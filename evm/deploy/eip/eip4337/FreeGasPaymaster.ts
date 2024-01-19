import { ethers } from 'hardhat'
import { DeployFunction } from 'hardhat-deploy/types'
import { HardhatRuntimeEnvironment } from 'hardhat/types'

const deploy: DeployFunction = async function (hre: HardhatRuntimeEnvironment) {
  const provider = ethers.provider
  const from = await provider.getSigner().getAddress()
  const ret = await hre.deployments.deploy(
    'FreeGasPaymaster', {
      from,
      args: ['0x5ff137d4b0fdcd49dca30c7cf57e578a026d2789'],
      gasLimit: 6e6,
      deterministicDeployment: false
    })
  console.log('==FreeGasPaymaster addr=', ret.address)
}

export default deploy
deploy.tags = ['FreeGasPaymaster']
