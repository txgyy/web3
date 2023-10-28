package xin.yukino.web3.contract.okc.create;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import xin.yukino.web3.util.IChain;
import xin.yukino.web3.util.TransactionUtil;

public class Create {

    public static EthSendTransaction create(String init, Credentials credentials, IChain chain) {
        return TransactionUtil.execute(chain, credentials, null, init);
    }
}
