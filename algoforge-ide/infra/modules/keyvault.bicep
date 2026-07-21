// Key Vault for secrets (RBAC authorization model). The API's managed identity is
// granted read access via a role assignment in rbac.bicep.

@description('Azure region.')
param location string

@description('Prefix for resource names.')
param namePrefix string

@description('Tenant ID for the vault.')
param tenantId string = subscription().tenantId

@description('Optional GitHub token to store as a secret (leave empty to skip).')
@secure()
param githubToken string = ''

@description('Optional GitHub Models token (AI chat) to store as a secret (leave empty to skip).')
@secure()
param githubModelsToken string = ''

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

// Store the GitHub token as a secret only when one is supplied.
resource githubTokenSecret 'Microsoft.KeyVault/vaults/secrets@2023-07-01' = if (!empty(githubToken)) {
  parent: keyVault
  name: 'github-token'
  properties: {
    value: githubToken
  }
}

// Store the GitHub Models token (AI chat) as a secret only when one is supplied.
resource githubModelsTokenSecret 'Microsoft.KeyVault/vaults/secrets@2023-07-01' = if (!empty(githubModelsToken)) {
  parent: keyVault
  name: 'github-models-token'
  properties: {
    value: githubModelsToken
  }
}

output keyVaultName string = keyVault.name
output keyVaultId string = keyVault.id
output keyVaultUri string = keyVault.properties.vaultUri
// Stable, version-less secret URI the Container App references (only valid when a token was stored).
output githubTokenSecretUri string = '${keyVault.properties.vaultUri}secrets/github-token'
// Stable, version-less secret URI for the GitHub Models token (only valid when stored).
output githubModelsTokenSecretUri string = '${keyVault.properties.vaultUri}secrets/github-models-token'

