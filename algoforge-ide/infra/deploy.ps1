# End-to-end deployment helper for AlgoForge IDE on Azure.
#
# Prerequisites: Azure CLI (`az login` done), Docker, Node. Run from repo/algoforge-ide.
# This performs hard-to-reverse actions (creates Azure resources that cost money).
# Review each step before running; you can run steps individually instead of all at once.

param(
  [string]$ResourceGroup = 'algoforge-rg',
  [string]$Location = 'eastus2',
  [string]$NamePrefix = 'algoforge',
  [Parameter(Mandatory = $true)][string]$SshPublicKeyPath  # e.g. $HOME\.ssh\id_ed25519.pub
)

$ErrorActionPreference = 'Stop'
$sshKey = (Get-Content $SshPublicKeyPath -Raw).Trim()

Write-Host "==> 1/6 Create resource group $ResourceGroup ($Location)"
az group create -n $ResourceGroup -l $Location | Out-Null

Write-Host "==> 2/6 Deploy base infrastructure (Bicep phase 1, no API app yet)"
$deploy = az deployment group create `
  -g $ResourceGroup `
  -f infra/main.bicep `
  -p infra/main.parameters.json `
  -p pistonSshPublicKey="$sshKey" namePrefix=$NamePrefix location=$Location deployApi=false `
  --query properties.outputs -o json | ConvertFrom-Json

$acrName = $deploy.acrName.value
$acrLoginServer = $deploy.acrLoginServer.value
$storageAccount = $deploy.storageAccountName.value

Write-Host "==> 3/6 Build & push API image to ACR"
az acr build -r $acrName -t "algoforge-api:latest" -f Dockerfile .

Write-Host "==> 4/6 Deploy the API Container App pointing at the real image (phase 2)"
$deploy2 = az deployment group create `
  -g $ResourceGroup `
  -f infra/main.bicep `
  -p infra/main.parameters.json `
  -p pistonSshPublicKey="$sshKey" namePrefix=$NamePrefix location=$Location `
     deployApi=true apiImage="$acrLoginServer/algoforge-api:latest" `
  --query properties.outputs -o json | ConvertFrom-Json
$apiUrl = $deploy2.apiUrl.value
$swaHost = $deploy2.staticWebAppHostname.value
Write-Host "    ACR=$acrLoginServer  Storage=$storageAccount  API=$apiUrl"

Write-Host "==> 5/6 Upload algorithm files to Blob storage"
$env:AZURE_STORAGE_ACCOUNT_URL = "https://$storageAccount.blob.core.windows.net"
npm run upload:blob

Write-Host "==> 6/6 Build & deploy the frontend to Static Web Apps"
# Build the UI to talk to the API origin, then deploy dist/ with the SWA CLI.
$env:VITE_API_BASE = $apiUrl
npm run build
$swaToken = az staticwebapp secrets list -n "$NamePrefix-web" -g $ResourceGroup --query properties.apiKey -o tsv
npx --yes @azure/static-web-apps-cli deploy ./dist --deployment-token $swaToken --env production

Write-Host "`nDone."
Write-Host "  Frontend: https://$swaHost"
Write-Host "  API:      $apiUrl"
