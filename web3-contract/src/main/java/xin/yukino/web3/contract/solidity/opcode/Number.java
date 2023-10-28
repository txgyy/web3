package xin.yukino.web3.contract.solidity.opcode;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.EthCall;
import xin.yukino.web3.util.IChain;
import xin.yukino.web3.util.TransactionUtil;

import java.util.List;

public class Number {

    public static EthCall numberMarker(String contract, String from, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList();
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Uint256.class));
        Function function = new Function("numberMarker", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        return TransactionUtil.call(from, contract, data, chain);
    }
}
