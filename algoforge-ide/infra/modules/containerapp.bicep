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

@description('Container port the API listens on.')
param targetPort int = 3001

var envName = '${namePrefix}-cae'
var appName = '${namePrefix}-api'

resource logAnalytics 'Microsoft.OperationalInsights/workspaces@2023-09-01' existing = {
  name: last(split(logAnalyticsWorkspaceId, '/'))
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

resource api 'Microsoft.App/containerApps@2024-03-01' = {
  name: appName
  location: location
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
          env: [
            { name: 'PORT', value: string(targetPort) }
            { name: 'PISTON_URL', value: pistonUrl }
            { name: 'FILE_SOURCE', value: 'blob' }
            { name: 'AZURE_STORAGE_ACCOUNT_URL', value: storageAccountUrl }
            { name: 'AZURE_STORAGE_CONTAINER', value: storageContainer }
            { name: 'COMPANY_FOLDERS', value: companyFolders }
            { name: 'ALLOWED_ORIGINS', value: allowedOrigins }
            { name: 'AZURE_CLIENT_ID', value: userAssignedClientId }
            { name: 'APPLICATIONINSIGHTS_CONNECTION_STRING', value: appInsightsConnectionString }
          ]
        }
      ]
      scale: {
        minReplicas: 0
        maxReplicas: 3
      }
    }
  }
}

output apiFqdn string = api.properties.configuration.ingress.fqdn
output apiName string = api.name
output environmentId string = env.id
