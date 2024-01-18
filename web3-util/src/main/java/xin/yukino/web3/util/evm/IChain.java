package xin.yukino.web3.util.evm;

import org.web3j.crypto.transaction.type.TransactionType;
import xin.yukino.web3.util.evm.web3j.Web3jDebug;


public interface IChain {

    long getChainId();

    TransactionType getTxType();

    Web3jDebug getWeb3j();

}
