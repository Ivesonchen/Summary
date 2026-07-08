// Virtual network with two subnets:
//   snet-apps   -> delegated to the Container Apps (workload profile) environment
//   snet-piston -> hosts the private Piston execution VM
//
// An NSG on the Piston subnet allows inbound only from the apps subnet on the
// Piston port, so the sandbox is never reachable from the public internet.

@description('Azure region for all resources.')
param location string

@description('Prefix for resource names.')
param namePrefix string

@description('Address space for the virtual network.')
param vnetAddressPrefix string = '10.20.0.0/16'

@description('Subnet for the Container Apps environment (min /23 for workload profiles + delegation).')
param appsSubnetPrefix string = '10.20.0.0/23'

@description('Subnet for the private Piston VM.')
param pistonSubnetPrefix string = '10.20.2.0/24'

@description('TCP port the Piston sandbox listens on.')
param pistonPort int = 2000

var vnetName = '${namePrefix}-vnet'
var pistonNsgName = '${namePrefix}-piston-nsg'

resource pistonNsg 'Microsoft.Network/networkSecurityGroups@2023-11-01' = {
  name: pistonNsgName
  location: location
  properties: {
    securityRules: [
      {
        name: 'Allow-Apps-To-Piston'
        properties: {
          priority: 100
          direction: 'Inbound'
          access: 'Allow'
          protocol: 'Tcp'
          sourceAddressPrefix: appsSubnetPrefix
          sourcePortRange: '*'
          destinationAddressPrefix: pistonSubnetPrefix
          destinationPortRange: string(pistonPort)
        }
      }
      {
        name: 'Deny-All-Inbound'
        properties: {
          priority: 4096
          direction: 'Inbound'
          access: 'Deny'
          protocol: '*'
          sourceAddressPrefix: '*'
          sourcePortRange: '*'
          destinationAddressPrefix: '*'
          destinationPortRange: '*'
        }
      }
    ]
  }
}

resource vnet 'Microsoft.Network/virtualNetworks@2023-11-01' = {
  name: vnetName
  location: location
  properties: {
    addressSpace: {
      addressPrefixes: [vnetAddressPrefix]
    }
    subnets: [
      {
        name: 'snet-apps'
        properties: {
          addressPrefix: appsSubnetPrefix
          delegations: [
            {
              name: 'containerapps-delegation'
              properties: {
                serviceName: 'Microsoft.App/environments'
              }
            }
          ]
        }
      }
      {
        name: 'snet-piston'
        properties: {
          addressPrefix: pistonSubnetPrefix
          networkSecurityGroup: {
            id: pistonNsg.id
          }
        }
      }
    ]
  }
}

output vnetId string = vnet.id
output appsSubnetId string = vnet.properties.subnets[0].id
output pistonSubnetId string = vnet.properties.subnets[1].id
output pistonSubnetPrefix string = pistonSubnetPrefix
