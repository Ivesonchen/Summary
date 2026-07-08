// Azure Container Registry for the API image.

@description('Azure region.')
param location string

@description('Prefix for resource names.')
param namePrefix string

@description('ACR SKU.')
@allowed(['Basic', 'Standard', 'Premium'])
param sku string = 'Basic'

// ACR names: 5-50 chars, alphanumeric, globally unique.
var acrName = toLower(replace('${namePrefix}acr${uniqueString(resourceGroup().id)}', '-', ''))

resource acr 'Microsoft.ContainerRegistry/registries@2023-11-01-preview' = {
  name: length(acrName) > 50 ? substring(acrName, 0, 50) : acrName
  location: location
  sku: {
    name: sku
  }
  properties: {
    adminUserEnabled: false
  }
}

output acrName string = acr.name
output acrLoginServer string = acr.properties.loginServer
output acrId string = acr.id
