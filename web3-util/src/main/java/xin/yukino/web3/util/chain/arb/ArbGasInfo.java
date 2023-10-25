package xin.yukino.web3.util.chain.arb;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.EthCall;
import xin.yukino.web3.util.chain.IChain;
import xin.yukino.web3.util.TransactionUtil;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class ArbGasInfo {

    public static final String ARB_GAS_INFO_ADDRESS = "0x000000000000000000000000000000000000006c";

    /**
     * @return per L2 tx,
     * @return per L1 calldata byte
     * @return per storage allocation,
     * @return per ArbGas base,
     * @return per ArbGas congestion,
     * @return per ArbGas total
     */
    public static List<BigInteger> getPricesInWei(IChain chain) {
        List<Type> inputParameters = Lists.newArrayList();
        List<TypeReference<?>> outputParameters = Lists.newArrayList(
                TypeReference.create(Uint256.class),
                TypeReference.create(Uint256.class),
                TypeReference.create(Uint256.class),
                TypeReference.create(Uint256.class),
                TypeReference.create(Uint256.class),
                TypeReference.create(Uint256.class)
        );
        Function function = new Function("getPricesInWei", inputParameters, outputParameters);
        EthCall call = TransactionUtil.call(ARB_GAS_INFO_ADDRESS, ARB_GAS_INFO_ADDRESS, FunctionEncoder.encode(function), chain);
        List<Type> result = FunctionReturnDecoder.decode(call.getValue(), function.getOutputParameters());
        return result.stream().map(t -> (BigInteger) t.getValue()).collect(Collectors.toList());
    }

    public static BigInteger getL2PriceInWei(IChain chain) {
        return getPricesInWei(chain).get(5);
    }

    public static BigInteger getL1PriceInWei(IChain chain) {
        return getPricesInWei(chain).get(1);
    }

    public static BigInteger getL1BaseFeeEstimate(IChain chain) {
        List<Type> inputParameters = Lists.newArrayList();
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Uint256.class));
        Function function = new Function("getL1BaseFeeEstimate", inputParameters, outputParameters);
        EthCall call = TransactionUtil.call(ARB_GAS_INFO_ADDRESS, ARB_GAS_INFO_ADDRESS, FunctionEncoder.encode(function), chain);
        List<Type> result = FunctionReturnDecoder.decode(call.getValue(), function.getOutputParameters());
        return ((BigInteger) result.get(0).getValue());
    }
}
