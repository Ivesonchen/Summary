// Key Vault for secrets (RBAC authorization model). The API's managed identity is
// granted read access via a role assignment in rbac.bicep.

@description('Azure region.')
param location string

@description('Prefix for resource names.')
param namePrefix string

@description('Tenant ID for the vault.')
param tenantId string = subscription().tenantId

var kvName = take(toLower(replace('${namePrefix}kv${uniqueString(resourceGroup().id)}', '-', '')), 24)

resource keyVault 'Microsoft.KeyVault/vaults@2023-07-01' = {
  name: kvName
  location: location
  properties: {
    tenantId: tenantId
    sku: {
      family: 'A'
      name: 'standard'
    }
    enableRbacAuthorization: true
    enableSoftDelete: true
    softDeleteRetentionInDays: 7
    publicNetworkAccess: 'Enabled'
  }
}

output keyVaultName string = keyVault.name
output keyVaultId string = keyVault.id
output keyVaultUri string = keyVault.properties.vaultUri
