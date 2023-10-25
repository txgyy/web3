package xin.yukino.web3.util.chain.op;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.methods.response.EthCall;
import xin.yukino.web3.util.chain.IChain;
import xin.yukino.web3.util.TransactionUtil;

import java.math.BigInteger;

public class OVMGasPriceOracle {

    public static final String GAS_PRICE_ORACLE = "0x420000000000000000000000000000000000000F";

    public static BigInteger estimateL1Fee(BigInteger nonce, BigInteger gasLimit, String to, BigInteger value, String data,
                                           BigInteger maxPriorityFeePerGas,
                                           BigInteger maxFeePerGas,
                                           IChain chain) {
        RawTransaction rawTransaction = RawTransaction.createTransaction(chain.getChainId(), nonce, gasLimit, to, value, data, maxPriorityFeePerGas, maxFeePerGas);
        byte[] rlpEncode = TransactionEncoder.encode(rawTransaction);
        return estimateL1Fee(rlpEncode, chain);
    }

    public static BigInteger estimateL1Fee(byte[] bytes, IChain chain) {
        DynamicBytes data = new DynamicBytes(bytes);
        Function func = new Function("getL1Fee", Lists.newArrayList(data), Lists.newArrayList(TypeReference.create(Uint256.class)));
        EthCall call = TransactionUtil.call(GAS_PRICE_ORACLE, GAS_PRICE_ORACLE, FunctionEncoder.encode(func), chain);
        return ((Uint256) FunctionReturnDecoder.decode(call.getValue(), func.getOutputParameters()).get(0)).getValue();
    }

    public static BigInteger estimateL1GasUsed(BigInteger nonce, BigInteger gasLimit, String to, BigInteger value, String data,
                                               BigInteger maxPriorityFeePerGas,
                                               BigInteger maxFeePerGas,
                                               IChain chain) {
        RawTransaction rawTransaction = RawTransaction.createTransaction(chain.getChainId(), nonce, gasLimit, to, value, data, maxPriorityFeePerGas, maxFeePerGas);
        byte[] rlpEncode = TransactionEncoder.encode(rawTransaction);
        return estimateL1GasUsed(rlpEncode, chain);
    }

    public static BigInteger estimateL1GasUsed(byte[] bytes, IChain chain) {
        DynamicBytes data = new DynamicBytes(bytes);
        Function func = new Function("getL1GasUsed", Lists.newArrayList(data), Lists.newArrayList(TypeReference.create(Uint256.class)));
        EthCall call = TransactionUtil.call(GAS_PRICE_ORACLE, GAS_PRICE_ORACLE, FunctionEncoder.encode(func), chain);
        return ((Uint256) FunctionReturnDecoder.decode(call.getValue(), func.getOutputParameters()).get(0)).getValue();
    }

    public static BigInteger getL1BaseFee(IChain chain) {
        Function func = new Function("l1BaseFee", Lists.newArrayList(), Lists.newArrayList(TypeReference.create(Uint256.class)));
        EthCall call = TransactionUtil.call(GAS_PRICE_ORACLE, GAS_PRICE_ORACLE, FunctionEncoder.encode(func), chain);
        return ((Uint256) FunctionReturnDecoder.decode(call.getValue(), func.getOutputParameters()).get(0)).getValue();
    }
}
