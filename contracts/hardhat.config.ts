import '@nomiclabs/hardhat-waffle'
import '@typechain/hardhat'
import { HardhatUserConfig } from 'hardhat/config'
import 'hardhat-deploy'
import '@nomiclabs/hardhat-etherscan'
import 'solidity-coverage'
import 'dotenv/config'
import 'hardhat-tracer'

const privateKey: string = process.env.PRIVATE_KEY!

function getNetwork (url: string): { url: string, accounts: [string] } {
  return {
    url,
    accounts: [privateKey]
  }
}

const optimizedCompilerSettings_0_8_12 = {
  version: '0.8.12',
  settings: {
    optimizer: {
      enabled: true,
      runs: 1000000
    }
    // viaIR: true
  }
}

// You need to export an object to set up your config
// Go to https://hardhat.org/config/ to learn more

const config: HardhatUserConfig = {
  solidity: {
    compilers: [{
      version: '0.8.17',
      settings: {
        optimizer: {
          enabled: false,
          runs: 0
        }
      }
    }],
    overrides: {
      'contracts/eip/eip4337/*': optimizedCompilerSettings_0_8_12
    }
  },
  networks: {
    okc_test: getNetwork('https://exchaintestrpc.okex.org'),
    okc_main: getNetwork('https://exchainrpc.okex.org'),
    eth_test: getNetwork('https://rpc.ankr.com/eth_goerli'),
    arb_test: getNetwork('https://goerli-rollup.arbitrum.io/rpc'),
    arb_main: getNetwork('https://arb1.arbitrum.io/rpc'),
    op_test: getNetwork('https://optimism-goerli.public.blastapi.io'),
    op_main: getNetwork('https://mainnet.optimism.io'),
    avax_main: getNetwork('https://avalanche.blockpi.network/v1/rpc/public'),
    matic_main: getNetwork('https://polygon-rpc.com')
  },
  mocha: {
    timeout: 10000
  }

}

// coverage chokes on the "compilers" settings
if (process.env.COVERAGE != null) {
  // @ts-ignore
  config.solidity = config.solidity.compilers[0]
}

export default config
