// Private Linux VM running the Piston execution sandbox.
// No public IP: reachable only from the apps subnet via the NSG on port 2000.
// cloud-init installs Docker, runs Piston, and installs the language runtimes.
// Ubuntu 22.04 uses cgroup v2, which Piston requires.

@description('Azure region.')
param location string

@description('Prefix for resource names.')
param namePrefix string

@description('Resource ID of the subnet to place the VM NIC in (snet-piston).')
param subnetId string

@description('VM size.')
param vmSize string = 'Standard_B2s'

@description('Admin username for SSH.')
param adminUsername string

@description('SSH public key for the admin user.')
@secure()
param sshPublicKey string

@description('Port Piston listens on.')
param pistonPort int = 2000

var vmName = '${namePrefix}-piston-vm'
var nicName = '${namePrefix}-piston-nic'

// cloud-init: install Docker, run Piston, then install language runtimes.
var cloudInit = '''
#cloud-config
package_update: true
packages:
  - ca-certificates
  - curl
runcmd:
  - install -m 0755 -d /etc/apt/keyrings
  - curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
  - chmod a+r /etc/apt/keyrings/docker.asc
  - echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo $VERSION_CODENAME) stable" > /etc/apt/sources.list.d/docker.list
  - apt-get update
  - apt-get install -y docker-ce docker-ce-cli containerd.io
  - systemctl enable --now docker
  - docker volume create piston-packages
  - docker run -d --name piston --privileged --restart always -p 2000:2000 -e PISTON_RUN_TIMEOUT=10000 -e PISTON_COMPILE_TIMEOUT=20000 -e PISTON_RUN_CPU_TIME=10000 -e PISTON_COMPILE_CPU_TIME=20000 -e PISTON_OUTPUT_MAX_SIZE=1048576 -v piston-packages:/piston/packages ghcr.io/engineer-man/piston
  - |
    for i in $(seq 1 60); do
      curl -sf http://localhost:2000/api/v2/runtimes && break || sleep 5
    done
  - |
    for pkg in 'python=3.12.0' 'java=15.0.2' 'node=20.11.1' 'typescript=5.0.3' 'gcc=10.2.0' 'go=1.16.2'; do
      lang="${pkg%%=*}"; ver="${pkg##*=}";
      curl -sf -X POST http://localhost:2000/api/v2/packages -H 'Content-Type: application/json' -d "{\"language\":\"$lang\",\"version\":\"$ver\"}" || true;
    done
'''

resource nic 'Microsoft.Network/networkInterfaces@2023-11-01' = {
  name: nicName
  location: location
  properties: {
    ipConfigurations: [
      {
        name: 'ipconfig1'
        properties: {
          privateIPAllocationMethod: 'Dynamic'
          subnet: {
            id: subnetId
          }
        }
      }
    ]
  }
}

resource vm 'Microsoft.Compute/virtualMachines@2023-09-01' = {
  name: vmName
  location: location
  properties: {
    hardwareProfile: {
      vmSize: vmSize
    }
    osProfile: {
      computerName: vmName
      adminUsername: adminUsername
      customData: base64(cloudInit)
      linuxConfiguration: {
        disablePasswordAuthentication: true
        ssh: {
          publicKeys: [
            {
              path: '/home/${adminUsername}/.ssh/authorized_keys'
              keyData: sshPublicKey
            }
          ]
        }
      }
    }
    storageProfile: {
      imageReference: {
        publisher: 'Canonical'
        offer: '0001-com-ubuntu-server-jammy'
        sku: '22_04-lts-gen2'
        version: 'latest'
      }
      osDisk: {
        createOption: 'FromImage'
        managedDisk: {
          storageAccountType: 'StandardSSD_LRS'
        }
      }
    }
    networkProfile: {
      networkInterfaces: [
        {
          id: nic.id
        }
      ]
    }
  }
}

// Private hostname other resources in the VNet use to reach Piston.
output pistonPrivateIp string = nic.properties.ipConfigurations[0].properties.privateIPAddress
output pistonUrl string = 'http://${nic.properties.ipConfigurations[0].properties.privateIPAddress}:${pistonPort}'
output vmName string = vm.name
