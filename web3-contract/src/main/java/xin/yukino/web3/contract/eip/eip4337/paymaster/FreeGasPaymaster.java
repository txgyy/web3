package xin.yukino.web3.contract.eip.eip4337.paymaster;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.protocol.core.methods.response.EthCall;
import xin.yukino.web3.util.IChain;
import xin.yukino.web3.util.TransactionUtil;

public class FreeGasPaymaster {

    public static String verifyingSigner(String paymaster, IChain chain) {
        Function function = new Function("verifyingSigner", Lists.newArrayList(), Lists.newArrayList(TypeReference.create(Address.class)));
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(null, paymaster, data, chain);
        return (String) FunctionReturnDecoder.decode(call.getResult(), function.getOutputParameters()).get(0).getValue();
    }
}
