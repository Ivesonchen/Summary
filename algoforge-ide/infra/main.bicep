// AlgoForge IDE — Azure infrastructure orchestrator (resource-group scope).
//
// Deploy:
//   az group create -n <rg> -l <location>
//   az deployment group create -g <rg> -f infra/main.bicep -p infra/main.parameters.json
//
// Topology:
//   VNet (snet-apps delegated to Container Apps, snet-piston with NSG)
//   Piston sandbox VM  (private, snet-piston)
//   Container Apps env + API app (snet-apps, system identity)
//   Storage (Blob) + ACR + Key Vault + Log Analytics/App Insights
//   Static Web App (frontend)
//   RBAC: API identity -> Blob Data Reader, AcrPull, KV Secrets User

targetScope = 'resourceGroup'

@description('Short name prefix for all resources (lowercase letters/digits).')
param namePrefix string = 'algoforge'

@description('Primary region for most resources.')
param location string = resourceGroup().location

@description('Region for the Static Web App (must be a SWA-supported region).')
param staticWebAppLocation string = 'eastus2'

@description('Admin username for the Piston VM.')
param pistonAdminUsername string = 'azureuser'

@description('SSH public key for the Piston VM admin user.')
@secure()
param pistonSshPublicKey string

@description('Piston VM size.')
param pistonVmSize string = 'Standard_B2s'

@description('Comma-separated company folder names.')
param companyFolders string = 'Amazon'

@description('Comma-separated allowed CORS origins (the deployed frontend URL). "*" allows any.')
param allowedOrigins string = ''

@description('GitHub repo (owner/name) for the sync feature. Empty leaves it configurable at runtime.')
param githubRepo string = ''

@description('GitHub branch for sync.')
param githubBranch string = 'master'

@description('Dedicated integration branch that sync commits land on (PR head).')
param githubSyncBranch string = 'algoforge-sync'

@description('GitHub token for sync. Stored in Key Vault and injected as a Container App secret. Leave empty to configure at runtime instead.')
@secure()
param githubToken string = ''

@description('GitHub Models token (AI chat). Stored in Key Vault and injected as a Container App secret. Leave empty to disable AI chat.')
@secure()
param githubModelsToken string = ''

@description('GitHub Models model id for the AI chat (empty uses the server default openai/gpt-4o-mini).')
param githubModelsModel string = ''

@description('AI provider for the chat tab: "github-models" (default) or "copilot".')
param aiProvider string = 'github-models'

@description('Copilot model id (used when aiProvider=copilot), e.g. claude-opus-4.8.')
param copilotModel string = ''

@description('Deploy the API Container App. Set false for the first pass (before the image exists), true once the real image is in ACR.')
param deployApi bool = false

@description('Create the RBAC role assignments. Set false on redeploys where the roles already exist (avoids RoleAssignmentExists).')
param deployRbac bool = true

@description('Full API image reference to run when deployApi is true (e.g. myacr.azurecr.io/algoforge-api:latest).')
param apiImage string = ''

var pistonPort = 2000
var hasGithubToken = !empty(githubToken)
var hasGithubModelsToken = !empty(githubModelsToken)

module network 'modules/network.bicep' = {
  name: 'network'
  params: {
    location: location
    namePrefix: namePrefix
    pistonPort: pistonPort
  }
}

module monitoring 'modules/monitoring.bicep' = {
  name: 'monitoring'
  params: {
    location: location
    namePrefix: namePrefix
  }
}

module storage 'modules/storage.bicep' = {
  name: 'storage'
  params: {
    location: location
    namePrefix: namePrefix
  }
}

module acr 'modules/acr.bicep' = {
  name: 'acr'
  params: {
    location: location
    namePrefix: namePrefix
  }
}

module keyVault 'modules/keyvault.bicep' = {
  name: 'keyvault'
  params: {
    location: location
    namePrefix: namePrefix
    githubToken: githubToken
    githubModelsToken: githubModelsToken
  }
}

module pistonVm 'modules/piston-vm.bicep' = {
  name: 'piston-vm'
  params: {
    location: location
    namePrefix: namePrefix
    subnetId: network.outputs.pistonSubnetId
    vmSize: pistonVmSize
    adminUsername: pistonAdminUsername
    sshPublicKey: pistonSshPublicKey
    pistonPort: pistonPort
  }
}

// User-assigned identity for the API — created up front so its roles exist
// before the container app pulls its image (avoids the first-pull 401 deadlock).
module identity 'modules/identity.bicep' = {
  name: 'identity'
  params: {
    location: location
    namePrefix: namePrefix
  }
}

// Grant the API identity its roles. Runs regardless of deployApi so the roles
// are in place (and propagated) before the container app is ever created.
// Skippable (deployRbac=false) on redeploys where the assignments already exist.
module rbac 'modules/rbac.bicep' = if (deployRbac) {
  name: 'rbac'
  params: {
    principalId: identity.outputs.principalId
    storageAccountName: storage.outputs.storageAccountName
    acrName: acr.outputs.acrName
    keyVaultName: keyVault.outputs.keyVaultName
  }
}

module containerApp 'modules/containerapp.bicep' = if (deployApi) {
  name: 'containerapp'
  dependsOn: deployRbac
    ? [rbac] // ensure AcrPull/Blob roles exist before the app pulls its image
    : []
  params: {
    location: location
    namePrefix: namePrefix
    infrastructureSubnetId: network.outputs.appsSubnetId
    logAnalyticsWorkspaceId: monitoring.outputs.logAnalyticsId
    acrLoginServer: acr.outputs.acrLoginServer
    containerImage: apiImage
    userAssignedIdentityId: identity.outputs.id
    userAssignedClientId: identity.outputs.clientId
    pistonUrl: pistonVm.outputs.pistonUrl
    storageAccountUrl: storage.outputs.blobEndpoint
    storageContainer: storage.outputs.containerName
    companyFolders: companyFolders
    allowedOrigins: allowedOrigins
    githubRepo: githubRepo
    githubBranch: githubBranch
    githubSyncBranch: githubSyncBranch
    githubTokenSecretUri: hasGithubToken ? keyVault.outputs.githubTokenSecretUri : ''
    githubModelsTokenSecretUri: hasGithubModelsToken ? keyVault.outputs.githubModelsTokenSecretUri : ''
    githubModelsModel: githubModelsModel
    aiProvider: aiProvider
    copilotModel: copilotModel
    storageAccountName: storage.outputs.storageAccountName
    copilotShareName: storage.outputs.copilotShareName
    appInsightsConnectionString: monitoring.outputs.appInsightsConnectionString
  }
}

module staticWebApp 'modules/staticwebapp.bicep' = {
  name: 'staticwebapp'
  params: {
    location: staticWebAppLocation
    namePrefix: namePrefix
  }
}

output apiFqdn string = deployApi ? containerApp!.outputs.apiFqdn : ''
output apiUrl string = deployApi ? 'https://${containerApp!.outputs.apiFqdn}' : ''
output staticWebAppHostname string = staticWebApp.outputs.defaultHostname
output acrLoginServer string = acr.outputs.acrLoginServer
output acrName string = acr.outputs.acrName
output storageAccountName string = storage.outputs.storageAccountName
output storageAccountUrl string = storage.outputs.blobEndpoint
output containerAppName string = deployApi ? containerApp!.outputs.apiName : '${namePrefix}-api'
output staticWebAppName string = staticWebApp.outputs.staticWebAppName
output pistonPrivateUrl string = pistonVm.outputs.pistonUrl
output keyVaultName string = keyVault.outputs.keyVaultName
output resourceGroupName string = resourceGroup().name
