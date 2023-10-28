package xin.yukino.web3.contract.okc.aggregatelending;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import xin.yukino.web3.util.IChain;
import xin.yukino.web3.util.TransactionUtil;

public class Config {

    public static EthSendTransaction setUsingAsCollateral(String contract, String account, String asset, boolean usingAsCollateral, Credentials sender, IChain chain) {
        Function function = new Function("setUsingAsCollateral", Lists.newArrayList(new Address(account), new Address(asset), new Bool(usingAsCollateral)), Lists.newArrayList());
        String data = FunctionEncoder.encode(function);
        return TransactionUtil.execute(contract, data, sender, chain);
    }
}
