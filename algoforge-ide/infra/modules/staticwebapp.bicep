// Static Web App hosting the built React frontend. The frontend calls the API
// via its configured origin (set at build time or through SWA linked backends).

@description('Azure region for the Static Web App (limited set; e.g. eastus2, westus2, centralus).')
param location string

@description('Prefix for resource names.')
param namePrefix string

@description('SKU for the Static Web App.')
@allowed(['Free', 'Standard'])
param sku string = 'Free'

resource staticWebApp 'Microsoft.Web/staticSites@2023-12-01' = {
  name: '${namePrefix}-web'
  location: location
  sku: {
    name: sku
    tier: sku
  }
  properties: {
    // Deployed via GitHub Actions / SWA CLI rather than a repo build integration here.
    stagingEnvironmentPolicy: 'Enabled'
    allowConfigFileUpdates: true
  }
}

output staticWebAppName string = staticWebApp.name
output defaultHostname string = staticWebApp.properties.defaultHostname
