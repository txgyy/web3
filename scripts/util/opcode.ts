import { ethers } from 'hardhat'
import { BigNumberish } from '@ethersproject/bignumber'

// eslint-disable-next-line @typescript-eslint/explicit-function-return-type
async function main () {
  console.log(ethers.utils.getContractAddress({
    from: '0xc650c2EeFE3ed9Bf82A2f45ed7dE982Bb00a3949',
    nonce: 0
  }))
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error)
    process.exit(1)
  })
