// Container Apps environment (VNet-integrated, workload profiles) + the API app.
// The app uses a USER-ASSIGNED managed identity to pull from ACR and read Blob,
// so the roles can be granted before the app is created (avoids first-pull 401).

@description('Azure region.')
param location string

@description('Prefix for resource names.')
param namePrefix string

@description('Infrastructure subnet ID for the environment (snet-apps).')
param infrastructureSubnetId string

@description('Log Analytics workspace resource ID.')
param logAnalyticsWorkspaceId string

@description('ACR login server (e.g. myacr.azurecr.io).')
param acrLoginServer string

@description('Full container image reference to run (defaults to a public placeholder for first deploy).')
param containerImage string = 'mcr.microsoft.com/azuredocs/containerapps-helloworld:latest'

@description('Resource ID of the user-assigned managed identity for pull + Blob access.')
param userAssignedIdentityId string

@description('Client ID of the user-assigned managed identity (for DefaultAzureCredential).')
param userAssignedClientId string

@description('Private Piston sandbox URL (from the piston-vm module).')
param pistonUrl string

@description('Blob endpoint of the storage account holding algorithm files.')
param storageAccountUrl string

@description('Blob container name.')
param storageContainer string = 'algorithms'

@description('Comma-separated company folder names.')
param companyFolders string = 'Amazon'

@description('Comma-separated allowed CORS origins (the frontend URL).')
param allowedOrigins string = ''

@description('Application Insights connection string.')
param appInsightsConnectionString string = ''

@description('GitHub repo (owner/name) for the sync feature. Empty to leave unset.')
param githubRepo string = ''

@description('GitHub branch for sync.')
param githubBranch string = 'master'

@description('Dedicated integration branch that sync commits land on (PR head).')
param githubSyncBranch string = 'algoforge-sync'

@description('Key Vault secret URI holding the GitHub token. Empty to skip wiring the token.')
param githubTokenSecretUri string = ''

@description('Key Vault secret URI holding the GitHub Models token (AI chat). Empty to skip.')
param githubModelsTokenSecretUri string = ''

@description('GitHub Models model id for the AI chat (empty uses the server default).')
param githubModelsModel string = ''

@description('AI provider for the chat tab: "github-models" (default) or "copilot".')
param aiProvider string = 'github-models'

@description('Copilot model id (used when aiProvider=copilot), e.g. claude-opus-4.8.')
param copilotModel string = ''

@description('Storage account name (for the Copilot session Azure Files share).')
param storageAccountName string = ''

@description('Azure Files share name that persists the Copilot session (~/.copilot).')
param copilotShareName string = 'copilot-home'

@description('Container port the API listens on.')
param targetPort int = 3001

var envName = '${namePrefix}-cae'
var appName = '${namePrefix}-api'
// The copilot provider needs a persistent home volume + an always-on replica so
// the signed-in session (and any in-flight device-flow login) survive.
var copilotEnabled = toLower(aiProvider) == 'copilot'
var useGithubToken = !empty(githubTokenSecretUri)
var useGithubModelsToken = !empty(githubModelsTokenSecretUri)
var useGithubModelsModel = !empty(githubModelsModel)

// Base env vars, plus GitHub sync vars (token via Key Vault secret ref when present).
var baseEnv = [
  { name: 'PORT', value: string(targetPort) }
  { name: 'PISTON_URL', value: pistonUrl }
  { name: 'FILE_SOURCE', value: 'blob' }
  { name: 'AZURE_STORAGE_ACCOUNT_URL', value: storageAccountUrl }
  { name: 'AZURE_STORAGE_CONTAINER', value: storageContainer }
  { name: 'COMPANY_FOLDERS', value: companyFolders }
  { name: 'ALLOWED_ORIGINS', value: allowedOrigins }
  { name: 'AZURE_CLIENT_ID', value: userAssignedClientId }
  { name: 'APPLICATIONINSIGHTS_CONNECTION_STRING', value: appInsightsConnectionString }
  { name: 'GITHUB_REPO', value: githubRepo }
  { name: 'GITHUB_BRANCH', value: githubBranch }
  { name: 'GITHUB_SYNC_BRANCH', value: githubSyncBranch }
]
var githubTokenEnv = useGithubToken ? [{ name: 'GITHUB_TOKEN', secretRef: 'github-token' }] : []
var githubModelsTokenEnv = useGithubModelsToken ? [{ name: 'GITHUB_MODELS_TOKEN', secretRef: 'github-models-token' }] : []
var githubModelsModelEnv = useGithubModelsModel ? [{ name: 'GITHUB_MODELS_MODEL', value: githubModelsModel }] : []
var aiProviderEnv = [{ name: 'AI_PROVIDER', value: aiProvider }]
var copilotModelEnv = (copilotEnabled && !empty(copilotModel)) ? [{ name: 'COPILOT_MODEL', value: copilotModel }] : []
var appEnv = concat(baseEnv, githubTokenEnv, githubModelsTokenEnv, githubModelsModelEnv, aiProviderEnv, copilotModelEnv)
var appSecrets = concat(
  useGithubToken
    ? [{ name: 'github-token', keyVaultUrl: githubTokenSecretUri, identity: userAssignedIdentityId }]
    : [],
  useGithubModelsToken
    ? [{ name: 'github-models-token', keyVaultUrl: githubModelsTokenSecretUri, identity: userAssignedIdentityId }]
    : []
)


resource logAnalytics 'Microsoft.OperationalInsights/workspaces@2023-09-01' existing = {
  name: last(split(logAnalyticsWorkspaceId, '/'))
}

// Existing storage account — used to key the Copilot session Azure Files mount.
resource storageAcct 'Microsoft.Storage/storageAccounts@2023-05-01' existing = if (copilotEnabled) {
  name: storageAccountName
}

resource env 'Microsoft.App/managedEnvironments@2024-03-01' = {
  name: envName
  location: location
  properties: {
    appLogsConfiguration: {
      destination: 'log-analytics'
      logAnalyticsConfiguration: {
        customerId: logAnalytics.properties.customerId
        sharedKey: logAnalytics.listKeys().primarySharedKey
      }
    }
    vnetConfiguration: {
      infrastructureSubnetId: infrastructureSubnetId
    }
    workloadProfiles: [
      {
        name: 'Consumption'
        workloadProfileType: 'Consumption'
      }
    ]
  }
}

// Azure Files storage registered on the environment, mounted by the app to
// persist the Copilot CLI session (~/.copilot) across restarts.
resource copilotEnvStorage 'Microsoft.App/managedEnvironments/storages@2024-03-01' = if (copilotEnabled) {
  parent: env
  name: 'copilot-home'
  properties: {
    azureFile: {
      accountName: storageAccountName
      accountKey: storageAcct.listKeys().keys[0].value
      shareName: copilotShareName
      accessMode: 'ReadWrite'
    }
  }
}

var copilotVolumes = copilotEnabled
  ? [{ name: 'copilot-home', storageType: 'AzureFile', storageName: 'copilot-home' }]
  : []
var copilotVolumeMounts = copilotEnabled
  ? [{ volumeName: 'copilot-home', mountPath: '/home/node/.copilot' }]
  : []

resource api 'Microsoft.App/containerApps@2024-03-01' = {
  name: appName
  location: location
  dependsOn: copilotEnabled ? [copilotEnvStorage] : []
  identity: {
    type: 'UserAssigned'
    userAssignedIdentities: {
      '${userAssignedIdentityId}': {}
    }
  }
  properties: {
    managedEnvironmentId: env.id
    workloadProfileName: 'Consumption'
    configuration: {
      activeRevisionsMode: 'Single'
      ingress: {
        external: true
        targetPort: targetPort
        transport: 'auto'
        allowInsecure: false
      }
      secrets: appSecrets
      registries: [
        {
          server: acrLoginServer
          identity: userAssignedIdentityId
        }
      ]
    }
    template: {
      containers: [
        {
          name: 'api'
          image: containerImage
          resources: {
            cpu: json('0.5')
            memory: '1Gi'
          }
          env: appEnv
          volumeMounts: copilotVolumeMounts
        }
      ]
      volumes: copilotVolumes
      scale: {
        // Copilot needs a single always-on replica so the signed-in session and
        // any in-flight device-flow login stay on one instance.
        minReplicas: copilotEnabled ? 1 : 0
        maxReplicas: copilotEnabled ? 1 : 3
      }
    }
  }
}

output apiFqdn string = api.properties.configuration.ingress.fqdn
output apiName string = api.name
output environmentId string = env.id
