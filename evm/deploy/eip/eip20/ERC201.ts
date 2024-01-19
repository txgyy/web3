import { ethers } from 'hardhat'
import { DeployFunction } from 'hardhat-deploy/types'
import { HardhatRuntimeEnvironment } from 'hardhat/types'

const deploy: DeployFunction = async function (hre: HardhatRuntimeEnvironment) {
  const provider = ethers.provider
  const from = await provider.getSigner().getAddress()
  const ret = await hre.deployments.deploy(
    'ERC201', {
      from,
      args: [],
      gasLimit: 6e6,
      deterministicDeployment: true
    })
  console.log('==ERC201 addr=', ret.address)
}

export default deploy
deploy.tags = ['ERC201']
