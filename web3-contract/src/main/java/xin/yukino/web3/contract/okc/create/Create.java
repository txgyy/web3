package xin.yukino.web3.contract.okc.create;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import xin.yukino.web3.util.chain.ChainEnum;
import xin.yukino.web3.util.TransactionUtil;

public class Create {

    public static EthSendTransaction create(String init, Credentials credentials, ChainEnum chain) {
        return TransactionUtil.execute(null, init, credentials, chain);
    }
}
