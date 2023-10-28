package xin.yukino.web3.util;

import xin.yukino.web3.util.web3j.Web3jDebug;


public interface IChain {

    long getChainId();

    boolean isEip1559();

    Web3jDebug getWeb3j();

}
