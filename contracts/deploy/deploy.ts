import { ethers } from 'hardhat'
import { DeployFunction } from 'hardhat-deploy/types'
import { HardhatRuntimeEnvironment } from 'hardhat/types'

const deployEntryPoint: DeployFunction = async function (hre: HardhatRuntimeEnvironment) {
  const provider = ethers.provider
  const from = await provider.getSigner().getAddress()
  let ret
  ret = await hre.deployments.deploy(
    'PriceFeeds', {
      from,
      args: ['0xA39434A63A52E749F02807ae27335515BA4b07F7'],
      gasLimit: 6e6,
      deterministicDeployment: true
    })
  console.log('==PriceFeed addr=', ret.address)

  ret = await hre.deployments.deploy(
    'Msg', {
      from,
      args: [],
      gasLimit: 6e6,
      deterministicDeployment: true
    })
  console.log('==Msg addr=', ret.address)

  ret = await hre.deployments.deploy(
    'ERC201', {
      from,
      args: [],
      gasLimit: 6e6,
      deterministicDeployment: true
    })
  console.log('==ERC201 addr=', ret.address)
}

export default deployEntryPoint
