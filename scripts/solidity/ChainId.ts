import { ethers } from 'hardhat'

// eslint-disable-next-line @typescript-eslint/explicit-function-return-type
async function main () {
  const ChainId = await ethers.getContractFactory('ChainId')
  const chainId = await ChainId.deploy()
  console.log(chainId.address)
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error)
    process.exit(1)
  })
