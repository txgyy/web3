package xin.yukino.web3.contract.chain.arb;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.utils.Numeric;
import xin.yukino.web3.util.CodecUtil;
import xin.yukino.web3.util.TransactionUtil;
import xin.yukino.web3.util.IChain;
import xin.yukino.web3.util.constant.ChainIdConstant;
import xin.yukino.web3.util.constant.GasConstant;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class NodeInterface {

    public static final String NODE_INTERFACE_ADDRESS = "0x00000000000000000000000000000000000000c8";

    // 12125040168863184969
    private static final BigInteger RANDOM_NONCE = Numeric.toBigInt(CodecUtil.keccak256("Nonce".getBytes()), 0, 8);

    // 968523046
    private static final BigInteger RANDOM_GAS_TIP_CAP = Numeric.toBigInt(CodecUtil.keccak256("GasTipCap".getBytes()), 0, 4);

    // 4054464639
    private static final BigInteger RANDOM_GAS_FEE_CAP = Numeric.toBigInt(CodecUtil.keccak256("GasFeeCap".getBytes()), 0, 4);

    // 3091729570
    private static final BigInteger RANDOM_GAS = Numeric.toBigInt(CodecUtil.keccak256("Gas".getBytes()), 0, 4);

    private static final byte[] RAND_V = Numeric.toBytesPadded(BigInteger.valueOf(ChainIdConstant.ARB_MAIN * 3 + 35), 3);
    private static final byte[] RAND_R = CodecUtil.keccak256("R".getBytes());
    private static final byte[] RAND_S = CodecUtil.keccak256("S".getBytes());
    private static final Sign.SignatureData RAND_SIGNATURE = new Sign.SignatureData(RAND_V, RAND_R, RAND_S);

    private static final BigInteger ONE_IN_BIPS = BigInteger.valueOf(10000);
    private static final BigInteger GAS_ESTIMATION_L1_PRICE_PADDING = BigInteger.valueOf(11000);
    private static final int ESTIMATION_PADDING_UNITS = 16 * GasConstant.NON_ZERO_BYTE;
    private static final BigInteger ESTIMATION_PADDING_BASIS_POINTS = BigInteger.valueOf(10100);


    /**
     * @param data             the tx's calldata. Everything else like "From" and "Gas" are copied over
     * @param to               the tx's "To" (ignored when contractCreation is true)
     * @param contractCreation whether "To" is omitted
     * @return gasEstimate an estimate of the total amount of gas needed for this tx
     * @return gasEstimateForL1 an estimate of the amount of gas needed for the l1 component of this tx
     * @return baseFee the l2 base fee
     * @return l1BaseFeeEstimate ArbOS's l1 estimate of the l1 base fee
     */
    public static List<BigInteger> gasEstimateComponents(String from, String to, boolean contractCreation, byte[] data, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList(
                new Address(to),
                new Bool(contractCreation),
                new DynamicBytes(data)
        );
        List<TypeReference<?>> outputParameters = Lists.newArrayList(
                TypeReference.create(Uint64.class),
                TypeReference.create(Uint64.class),
                TypeReference.create(Uint256.class),
                TypeReference.create(Uint256.class)
        );
        Function function = new Function("gasEstimateComponents", inputParameters, outputParameters);
        EthCall call = TransactionUtil.call(from, NODE_INTERFACE_ADDRESS, FunctionEncoder.encode(function), chain);
        List<Type> result = FunctionReturnDecoder.decode(call.getValue(), function.getOutputParameters());
        return result.stream().map(t -> (BigInteger) t.getValue()).collect(Collectors.toList());
    }


    /**
     * @param data the tx's calldata. Everything else like "From" and "Gas" are copied over
     * @param to   the tx's "To" (ignored when contractCreation is true)
     * @return gasEstimateForL1 an estimate of the amount of gas needed for the l1 component of this tx
     * @return baseFee the l2 base fee
     * @return l1BaseFeeEstimate ArbOS's l1 estimate of the l1 base fee
     * @notice Estimates a transaction's l1 costs.
     * @dev Use eth_call to call.
     * This method is similar to gasEstimateComponents, but doesn't include the l2 component
     * so that the l1 component can be known even when the tx may fail.
     * This method also doesn't pad the estimate as gas estimation normally does.
     * If using this value to submit a transaction, we'd recommend first padding it by 10%.
     */
    public static BigInteger estimateL1GasUsed(String to, String data, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList(
                new Address(to),
                new Bool(false),
                new DynamicBytes(Numeric.hexStringToByteArray(data))
        );
        List<TypeReference<?>> outputParameters = Lists.newArrayList(
                TypeReference.create(Uint64.class),
                TypeReference.create(Uint256.class),
                TypeReference.create(Uint256.class)
        );
        Function function = new Function("gasEstimateL1Component", inputParameters, outputParameters);
        EthCall call = TransactionUtil.call(null, NODE_INTERFACE_ADDRESS, FunctionEncoder.encode(function), chain);
        List<BigInteger> result = FunctionReturnDecoder.decode(call.getValue(), function.getOutputParameters()).stream().map(t -> (BigInteger) t.getValue()).collect(Collectors.toList());
        return result.get(0).multiply(result.get(1)).divide(result.get(2));
    }

    public static BigInteger estimateL1GasUsedFromLocal(String to, String data, IChain chain) {
        RawTransaction rawTransaction = RawTransaction.createTransaction(0, RANDOM_NONCE, RANDOM_GAS, to, BigInteger.ZERO, data, RANDOM_GAS_TIP_CAP, RANDOM_GAS_FEE_CAP);
        byte[] rlpEncode = TransactionEncoder.encode(rawTransaction, RAND_SIGNATURE);
        byte[] compressed = CodecUtil.brotliFastCompress(rlpEncode);
        return BigInteger.valueOf((long) compressed.length * GasConstant.NON_ZERO_BYTE + ESTIMATION_PADDING_UNITS).multiply(ESTIMATION_PADDING_BASIS_POINTS).divide(ONE_IN_BIPS).multiply(GAS_ESTIMATION_L1_PRICE_PADDING).divide(ONE_IN_BIPS);
    }

}

