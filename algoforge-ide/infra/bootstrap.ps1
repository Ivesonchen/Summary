# ============================================================================
#  AlgoForge IDE - full auto bootstrap (Azure infra + GitHub Actions CI/CD)
# ============================================================================
# One command to go from zero to a GitOps-deployed app. It:
#   1. Runs preflight checks (az, gh, node, git + that you're logged in)
#   2. Ensures an SSH key for the Piston VM
#   3. Creates the resource group and deploys the Bicep infrastructure
#   4. Creates the GitHub OIDC identity + least-privilege role assignments
#   5. Reads deployment outputs and sets the GitHub repo Secrets & Variables
#   6. (optional) Commits & pushes to trigger the Deploy workflow
#
# The two things you MUST do yourself first (interactive security gates):
#   az login
#   gh auth login
#
# Usage:
#   ./infra/bootstrap.ps1 -Push            # do everything incl. push to deploy
#   ./infra/bootstrap.ps1                  # set everything up, don't push yet
#
# Re-runnable: role assignments and the Entra app are created idempotently.
# ============================================================================

[CmdletBinding()]
param(
  [string]$ResourceGroup = 'algoforge-rg',
  [string]$Location = 'eastus2',
  [string]$NamePrefix = 'algoforge',
  [string]$Repo = 'Ivesonchen/Summary',
  [string]$OidcAppName = 'algoforge-github-oidc',
  [string]$SshPublicKeyPath = "$HOME/.ssh/id_ed25519.pub",
  [switch]$Push
)

$ErrorActionPreference = 'Stop'
function Info($m) { Write-Host "==> $m" -ForegroundColor Cyan }
function Ok($m)   { Write-Host "  OK  $m" -ForegroundColor Green }
function Warn($m) { Write-Host "  !!  $m" -ForegroundColor Yellow }

# --- Step 1: preflight -----------------------------------------------------
Info '1/6 Preflight checks'
foreach ($tool in 'az', 'gh', 'node', 'git') {
  if (-not (Get-Command $tool -ErrorAction SilentlyContinue)) {
    throw "Required tool '$tool' not found on PATH. Install it and retry."
  }
}
# Azure login?
try { $acct = az account show -o json 2>$null | ConvertFrom-Json } catch {}
if (-not $acct) { throw "Not logged into Azure. Run 'az login' first." }
Ok "Azure subscription: $($acct.name) ($($acct.id))"
# GitHub login?
gh auth status 2>$null | Out-Null
if ($LASTEXITCODE -ne 0) { throw "Not logged into GitHub. Run 'gh auth login' first." }
Ok "GitHub authenticated for repo $Repo"

$subscriptionId = $acct.id
$tenantId = $acct.tenantId

# --- Step 2: SSH key -------------------------------------------------------
Info '2/6 Ensure SSH key for the Piston VM'
if (-not (Test-Path $SshPublicKeyPath)) {
  $priv = [IO.Path]::ChangeExtension($SshPublicKeyPath, $null).TrimEnd('.')
  Warn "No key at $SshPublicKeyPath - generating one."
  ssh-keygen -t ed25519 -f $priv -N '""' | Out-Null
}
$sshKey = (Get-Content $SshPublicKeyPath -Raw).Trim()
Ok 'SSH public key ready'

# --- Step 3: deploy infrastructure (two-phase) ----------------------------
Info "3/6 Deploy infrastructure to resource group '$ResourceGroup' ($Location)"
az group create -n $ResourceGroup -l $Location -o none

# Phase 1: everything except the API Container App (the real image doesn't exist yet).
$deploy1 = "algoforge-p1-$([DateTime]::UtcNow.ToString('yyyyMMddHHmmss'))"
az deployment group create `
  -g $ResourceGroup -n $deploy1 `
  -f "$PSScriptRoot/main.bicep" -p "$PSScriptRoot/main.parameters.json" `
  -p pistonSshPublicKey="$sshKey" namePrefix=$NamePrefix location=$Location deployApi=false `
  -o none
$out1 = az deployment group show -g $ResourceGroup -n $deploy1 --query properties.outputs -o json | ConvertFrom-Json
$acrName        = $out1.acrName.value
$acrLoginServer = $out1.acrLoginServer.value
$storageUrl     = $out1.storageAccountUrl.value
Ok 'Phase 1 (base infra) deployed'

# Build & push the real API image to ACR (listens on port 3001).
Info '    Building & pushing API image to ACR'
$imageRef = "$acrLoginServer/algoforge-api:latest"
az acr build --registry $acrName --image "algoforge-api:latest" `
  --file "$PSScriptRoot/../Dockerfile" "$PSScriptRoot/.." -o none
Ok "Image pushed: $imageRef"

# Phase 2: now create the Container App + RBAC pointing at the real image.
$deploy2 = "algoforge-p2-$([DateTime]::UtcNow.ToString('yyyyMMddHHmmss'))"
az deployment group create `
  -g $ResourceGroup -n $deploy2 `
  -f "$PSScriptRoot/main.bicep" -p "$PSScriptRoot/main.parameters.json" `
  -p pistonSshPublicKey="$sshKey" namePrefix=$NamePrefix location=$Location `
     deployApi=true apiImage="$imageRef" `
  -o none
$out = az deployment group show -g $ResourceGroup -n $deploy2 --query properties.outputs -o json | ConvertFrom-Json
Ok 'Phase 2 (API Container App) deployed'

$containerApp   = $out.containerAppName.value
$staticWebApp   = $out.staticWebAppName.value
$apiUrl         = $out.apiUrl.value
$swaHostname    = $out.staticWebAppHostname.value

# Seed the algorithm files into Blob so the app has data immediately.
Info '    Seeding algorithm files into Blob storage'
$storageAccountName = $out1.storageAccountName.value

# Grant the signed-in user Blob Data Contributor so the upload can write.
# (The container app uses its own managed identity; this is just for this seeding step.)
$myObjectId = az ad signed-in-user show --query id -o tsv
$storageId = az storage account show -g $ResourceGroup -n $storageAccountName --query id -o tsv
az role assignment create --assignee-object-id $myObjectId --assignee-principal-type User `
  --role 'Storage Blob Data Contributor' --scope $storageId -o none 2>$null
Ok 'Granted your user Blob write access; waiting for role propagation'
Start-Sleep -Seconds 60

$env:AZURE_STORAGE_ACCOUNT_URL = $storageUrl
$env:LOCAL_REPO_ROOT = (Resolve-Path "$PSScriptRoot/../..").Path
Push-Location "$PSScriptRoot/.."
if (-not (Test-Path 'node_modules')) { npm ci | Out-Null }
npm run --silent upload:blob
Pop-Location
Ok 'Algorithm files uploaded'

# --- Step 4: GitHub OIDC identity + roles ---------------------------------
Info '4/6 Create GitHub OIDC identity and role assignments'
$appId = az ad app list --display-name $OidcAppName --query "[0].appId" -o tsv
if (-not $appId) {
  $appId = az ad app create --display-name $OidcAppName --query appId -o tsv
  Ok "Created Entra app ($appId)"
} else {
  Ok "Reusing existing Entra app ($appId)"
}

# Ensure a service principal exists for the app. `az ad sp show` errors when the
# SP is missing, so query the list (which returns empty instead of erroring) and
# retry to allow for Entra propagation after a fresh app/SP creation.
$spObjectId = az ad sp list --filter "appId eq '$appId'" --query "[0].id" -o tsv 2>$null
if (-not $spObjectId) {
  az ad sp create --id $appId -o none 2>$null
  for ($i = 0; $i -lt 12 -and -not $spObjectId; $i++) {
    Start-Sleep -Seconds 5
    $spObjectId = az ad sp list --filter "appId eq '$appId'" --query "[0].id" -o tsv 2>$null
  }
}
if (-not $spObjectId) { throw "Service principal for app $appId did not become available. Re-run the script." }
Ok "Service principal ready ($spObjectId)"

# Federated credentials for master branch and PRs (idempotent).
$creds = @(
  @{ name = 'master'; subject = "repo:${Repo}:ref:refs/heads/master" },
  @{ name = 'prs';    subject = "repo:${Repo}:pull_request" }
)
$existing = az ad app federated-credential list --id $appId --query "[].name" -o tsv
foreach ($c in $creds) {
  if ($existing -notcontains $c.name) {
    $body = @{ name = $c.name; issuer = 'https://token.actions.githubusercontent.com'; subject = $c.subject; audiences = @('api://AzureADTokenExchange') } | ConvertTo-Json -Compress
    $body | az ad app federated-credential create --id $appId --parameters '@-' -o none
    Ok "Added federated credential '$($c.name)'"
  }
}

# Role assignments (idempotent - create returns error if exists, which we ignore).
$rgId = az group show -n $ResourceGroup --query id -o tsv
function Grant($role, $scope) {
  az role assignment create --assignee-object-id $spObjectId --assignee-principal-type ServicePrincipal --role $role --scope $scope -o none 2>$null
  Ok "Role '$role' granted"
}
Grant 'Contributor' $rgId
Grant 'AcrPush' $rgId
Grant 'Storage Blob Data Contributor' $rgId

# --- Step 5: set GitHub secrets & variables -------------------------------
Info '5/6 Configure GitHub repository secrets and variables'
$swaToken = az staticwebapp secrets list -n $staticWebApp -g $ResourceGroup --query properties.apiKey -o tsv

function Set-Secret($name, $value) { gh secret set $name -b "$value" -R $Repo | Out-Null; Ok "secret $name" }
function Set-Var($name, $value)    { gh variable set $name -b "$value" -R $Repo | Out-Null; Ok "variable $name" }

Set-Secret 'AZURE_CLIENT_ID' $appId
Set-Secret 'AZURE_TENANT_ID' $tenantId
Set-Secret 'AZURE_SUBSCRIPTION_ID' $subscriptionId
Set-Secret 'AZURE_STATIC_WEB_APPS_API_TOKEN' $swaToken

Set-Var 'AZURE_RESOURCE_GROUP' $ResourceGroup
Set-Var 'ACR_NAME' $acrName
Set-Var 'ACR_LOGIN_SERVER' $acrLoginServer
Set-Var 'CONTAINER_APP_NAME' $containerApp
Set-Var 'STATIC_WEB_APP_NAME' $staticWebApp
Set-Var 'AZURE_STORAGE_ACCOUNT_URL' $storageUrl
Set-Var 'API_URL' $apiUrl

# --- Step 6: trigger deployment -------------------------------------------
Info '6/6 Trigger deployment'
if ($Push) {
  Push-Location (Resolve-Path "$PSScriptRoot/../..")
  git add .github algoforge-ide
  git commit -m 'Deploy AlgoForge IDE (infra + CI/CD)' | Out-Null
  git push origin master
  Pop-Location
  Ok 'Pushed to master - GitHub Actions Deploy workflow is running.'
} else {
  Warn 'Skipped push. Review changes, then run:  git push origin master  (or re-run with -Push)'
  Warn "You can also trigger it manually: gh workflow run Deploy -R $Repo"
}

Write-Host "`n============================================================" -ForegroundColor Green
Write-Host " Setup complete." -ForegroundColor Green
Write-Host "   Frontend : https://$swaHostname" -ForegroundColor Green
Write-Host "   API      : $apiUrl" -ForegroundColor Green
Write-Host "   Watch    : gh run watch -R $Repo" -ForegroundColor Green
Write-Host "============================================================" -ForegroundColor Green
