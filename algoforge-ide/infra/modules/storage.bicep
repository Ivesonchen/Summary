// Storage account + blob container holding the algorithm source files.
// Public blob access is disabled; the API reads via managed identity (RBAC).

@description('Azure region.')
param location string

@description('Prefix for resource names.')
param namePrefix string

@description('Blob container name for algorithm files.')
param containerName string = 'algorithms'

// Storage account names: 3-24 chars, lowercase alphanumeric, globally unique.
var storageName = toLower(replace('${namePrefix}stor${uniqueString(resourceGroup().id)}', '-', ''))
var storageNameTrimmed = length(storageName) > 24 ? substring(storageName, 0, 24) : storageName

resource storage 'Microsoft.Storage/storageAccounts@2023-05-01' = {
  name: storageNameTrimmed
  location: location
  sku: {
    name: 'Standard_LRS'
  }
  kind: 'StorageV2'
  properties: {
    allowBlobPublicAccess: false
    minimumTlsVersion: 'TLS1_2'
    supportsHttpsTrafficOnly: true
    allowSharedKeyAccess: true
  }
}

resource blobService 'Microsoft.Storage/storageAccounts/blobServices@2023-05-01' = {
  parent: storage
  name: 'default'
}

resource container 'Microsoft.Storage/storageAccounts/blobServices/containers@2023-05-01' = {
  parent: blobService
  name: containerName
  properties: {
    publicAccess: 'None'
  }
}

output storageAccountName string = storage.name
output blobEndpoint string = storage.properties.primaryEndpoints.blob
output containerName string = containerName
