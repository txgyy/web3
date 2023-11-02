import { ethers } from 'hardhat'

// eslint-disable-next-line @typescript-eslint/explicit-function-return-type
async function main () {
  const Msg = await ethers.getContractFactory('Msg')
  const msg = await Msg.attach('0x2009F65340d13DbF483bf5526c71857E96B444bA')
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error)
    process.exit(1)
  })
