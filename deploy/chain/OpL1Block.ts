import { ethers } from 'hardhat'
import { DeployFunction } from 'hardhat-deploy/types'
import { HardhatRuntimeEnvironment } from 'hardhat/types'

const deploy: DeployFunction = async function (hre: HardhatRuntimeEnvironment) {
  const provider = ethers.provider
  const from = await provider.getSigner().getAddress()
  const ret = await hre.deployments.deploy(
    'OpL1Block', {
      from,
      args: [],
      gasLimit: 6e6,
      deterministicDeployment: true
    })
  console.log('==OpL1Block addr=', ret.address)
}

export default deploy
deploy.tags = ['OpL1Block']
