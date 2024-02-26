package xin.yukino.web3.app.evm;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import xin.yukino.web3.util.evm.IChain;
import xin.yukino.web3.util.evm.TransactionUtil;

public class Empty {

    public static EthSendTransaction t(String contract, Credentials bounder, IChain chain) {
        Function function = new Function("t", Lists.newArrayList(), Lists.newArrayList());
        return TransactionUtil.execute(chain, bounder, contract, FunctionEncoder.encode(function));
    }

    public static EthSendTransaction withdraw(String contract, Credentials bounder, IChain chain) {
        Function function = new Function("withdraw", Lists.newArrayList(), Lists.newArrayList());
        return TransactionUtil.execute(chain, bounder, contract, FunctionEncoder.encode(function));
    }
}
