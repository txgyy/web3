package xin.yukino.web3.app.evm.gas;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.EthCall;
import xin.yukino.web3.util.evm.IChain;
import xin.yukino.web3.util.evm.TransactionUtil;

import java.math.BigInteger;
import java.util.List;

public class GasCallData {

    public static BigInteger test(String contract, DynamicBytes b, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList(b);
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Uint256.class));
        Function function = new Function("test", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(contract, contract, data, chain);
        return ((Uint256) FunctionReturnDecoder.decode(call.getValue(), function.getOutputParameters()).get(0)).getValue();
    }

    public static BigInteger testEstimateGas(String contract, DynamicBytes b, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList(b);
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Uint256.class));
        Function function = new Function("test", inputParameters, outputParameters);
        return TransactionUtil.estimateGas(contract, contract, FunctionEncoder.encode(function), chain);
    }

    public static BigInteger test(String contract, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList();
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Uint256.class));
        Function function = new Function("test", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(contract, contract, data, chain);
        return ((Uint256) FunctionReturnDecoder.decode(call.getValue(), function.getOutputParameters()).get(0)).getValue();
    }

    public static BigInteger testEstimateGas(String contract, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList();
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Uint256.class));
        Function function = new Function("test", inputParameters, outputParameters);
        return TransactionUtil.estimateGas(contract, contract, FunctionEncoder.encode(function), chain);
    }
}
