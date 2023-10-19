package xin.yukino.web3.contract.eip.eip4337.account;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.EthCall;
import xin.yukino.web3.util.chain.ChainEnum;
import xin.yukino.web3.util.TransactionUtil;

import java.math.BigInteger;

public class Account {

    public static BigInteger nonce(String address, ChainEnum chain) {
        Function function = new Function("nonce", Lists.newArrayList(), Lists.newArrayList(TypeReference.create(Uint256.class)));
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(null, address, data, chain);
        return (BigInteger) FunctionReturnDecoder.decode(call.getResult(), function.getOutputParameters()).get(0).getValue();
    }

    public static String masterCopy(String address, ChainEnum chain) {
        Function function = new Function("masterCopy", Lists.newArrayList(), Lists.newArrayList(TypeReference.create(Address.class)));
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(null, address, data, chain);
        return (String) FunctionReturnDecoder.decode(call.getResult(), function.getOutputParameters()).get(0).getValue();
    }

    public static String getOwner(String address, ChainEnum chain) {
        Function function = new Function("getOwner", Lists.newArrayList(), Lists.newArrayList(TypeReference.create(Address.class)));
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(null, address, data, chain);
        return (String) FunctionReturnDecoder.decode(call.getResult(), function.getOutputParameters()).get(0).getValue();
    }
}


