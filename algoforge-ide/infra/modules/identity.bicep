// User-assigned managed identity for the API container app.
// Created independently of the container app so its RBAC roles (AcrPull, Blob
// read, KV secrets) can be granted BEFORE the app tries to pull its image —
// avoiding the system-identity chicken-and-egg where the pull 401s on first deploy.

@description('Azure region.')
param location string

@description('Prefix for resource names.')
param namePrefix string

resource uami 'Microsoft.ManagedIdentity/userAssignedIdentities@2023-01-31' = {
  name: '${namePrefix}-api-identity'
  location: location
}

output id string = uami.id
output principalId string = uami.properties.principalId
output clientId string = uami.properties.clientId
