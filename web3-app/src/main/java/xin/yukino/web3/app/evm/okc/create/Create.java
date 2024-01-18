package xin.yukino.web3.app.evm.okc.create;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import xin.yukino.web3.util.evm.IChain;
import xin.yukino.web3.util.evm.TransactionUtil;

public class Create {

    public static EthSendTransaction create(String init, Credentials credentials, IChain chain) {
        return TransactionUtil.execute(chain, credentials, null, init);
    }
}
