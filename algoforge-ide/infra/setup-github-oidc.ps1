# One-time setup for GitHub Actions -> Azure OIDC (passwordless CI/CD).
#
# Creates an Entra app + service principal, federated credentials for this repo's
# master branch and PRs, grants it Contributor on the resource group, and prints
# the GitHub repo SECRETS and VARIABLES to configure. Run after infra is deployed.
#
# Requires: az CLI (logged in with rights to create app registrations + role assignments).

param(
  [string]$ResourceGroup = 'algoforge-rg',
  [string]$AppName = 'algoforge-github-oidc',
  [string]$Repo = 'Ivesonchen/Summary',   # owner/repo
  [string]$NamePrefix = 'algoforge'
)

$ErrorActionPreference = 'Stop'

$subscriptionId = az account show --query id -o tsv
$tenantId = az account show --query tenantId -o tsv

Write-Host "==> Creating Entra application '$AppName'"
$appId = az ad app create --display-name $AppName --query appId -o tsv
az ad sp create --id $appId | Out-Null

Write-Host "==> Adding federated credentials for $Repo"
$subjects = @(
  @{ name = 'master';  subject = "repo:${Repo}:ref:refs/heads/master" },
  @{ name = 'prs';     subject = "repo:${Repo}:pull_request" }
)
foreach ($s in $subjects) {
  $body = @{
    name      = $s.name
    issuer    = 'https://token.actions.githubusercontent.com'
    subject   = $s.subject
    audiences = @('api://AzureADTokenExchange')
  } | ConvertTo-Json -Compress
  $body | az ad app federated-credential create --id $appId --parameters '@-' | Out-Null
}

Write-Host "==> Granting Contributor on resource group $ResourceGroup"
$spObjectId = az ad sp show --id $appId --query id -o tsv
$rgId = az group show -n $ResourceGroup --query id -o tsv
az role assignment create --assignee-object-id $spObjectId --assignee-principal-type ServicePrincipal `
  --role 'Contributor' --scope $rgId | Out-Null
# AcrPush so `az acr build` can push images.
az role assignment create --assignee-object-id $spObjectId --assignee-principal-type ServicePrincipal `
  --role 'AcrPush' --scope $rgId | Out-Null
# Storage Blob Data Contributor so the file-upload step can write blobs via OIDC.
az role assignment create --assignee-object-id $spObjectId --assignee-principal-type ServicePrincipal `
  --role 'Storage Blob Data Contributor' --scope $rgId | Out-Null

Write-Host "`n================ Configure these in GitHub (Settings > Secrets and variables > Actions) ================"
Write-Host "`nSECRETS:"
Write-Host "  AZURE_CLIENT_ID        = $appId"
Write-Host "  AZURE_TENANT_ID        = $tenantId"
Write-Host "  AZURE_SUBSCRIPTION_ID  = $subscriptionId"
Write-Host "  AZURE_STATIC_WEB_APPS_API_TOKEN = (az staticwebapp secrets list -n ${NamePrefix}-web -g $ResourceGroup --query properties.apiKey -o tsv)"
Write-Host "`nVARIABLES:"
Write-Host "  AZURE_RESOURCE_GROUP        = $ResourceGroup"
Write-Host "  ACR_NAME                    = (from infra output acrName)"
Write-Host "  ACR_LOGIN_SERVER            = (from infra output acrLoginServer)"
Write-Host "  CONTAINER_APP_NAME          = ${NamePrefix}-api"
Write-Host "  STATIC_WEB_APP_NAME         = ${NamePrefix}-web"
Write-Host "  AZURE_STORAGE_ACCOUNT_URL   = (from infra output; https://<storage>.blob.core.windows.net)"
Write-Host "  API_URL                     = (from infra output apiUrl)"
