package xin.yukino.web3.contract.eip;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;
import xin.yukino.web3.util.IChain;
import xin.yukino.web3.util.TransactionUtil;

public class EIP1167 {

    public static EthSendTransaction createMinProxy(String contractAddress, Credentials credentials, IChain chain) {
        contractAddress = Numeric.cleanHexPrefix(contractAddress);
        String init = "0x3d602d80600a3d3981f3" + "363d3d373d3d3d363d73" + contractAddress + "5af43d82803e903d91602b57fd5bf3";
        return TransactionUtil.execute(chain, credentials, null, init);
    }
}
