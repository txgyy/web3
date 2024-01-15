package xin.yukino.web3.util;

import org.web3j.crypto.transaction.type.TransactionType;
import xin.yukino.web3.util.web3j.Web3jDebug;


public interface IChain {

    long getChainId();

    TransactionType getTxType();

    Web3jDebug getWeb3j();

}
