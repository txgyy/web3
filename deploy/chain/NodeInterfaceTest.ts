import { ethers } from 'hardhat'
import { DeployFunction } from 'hardhat-deploy/types'
import { HardhatRuntimeEnvironment } from 'hardhat/types'

const deploy: DeployFunction = async function (hre: HardhatRuntimeEnvironment) {
  const provider = ethers.provider
  const from = await provider.getSigner().getAddress()
  const ret = await hre.deployments.deploy(
    'NodeInterfaceTest', {
      from,
      args: [],
      gasLimit: 6e6,
      deterministicDeployment: true
    })
  console.log('==NodeInterfaceTest addr=', ret.address)
}

export default deploy
deploy.tags = ['NodeInterfaceTest']
