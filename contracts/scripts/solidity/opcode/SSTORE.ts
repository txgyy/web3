import { ethers } from 'hardhat'

// eslint-disable-next-line @typescript-eslint/explicit-function-return-type
async function main () {
  const Contract = await ethers.getContractFactory('SSTORE')
  const contract = await Contract.attach('0x9745Edbe02A7A26E95024F6a9d927B85Bc1402F5')
  contract.reset()
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error)
    process.exit(1)
  })
