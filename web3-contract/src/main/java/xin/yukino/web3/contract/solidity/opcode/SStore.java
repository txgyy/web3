package xin.yukino.web3.contract.solidity.opcode;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import xin.yukino.web3.util.chain.ChainEnum;
import xin.yukino.web3.util.Web3ErrorUtil;
import xin.yukino.web3.util.TransactionUtil;

import java.math.BigInteger;
import java.util.List;

public class SStore {

    public static BigInteger estimateSet(String contract, BigInteger gasLimit, Credentials sender, ChainEnum chain) {
        List<Type> inputParameters = Lists.newArrayList(new Uint256(gasLimit));
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Uint256.class));
        Function function = new Function("estimateSet", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(sender.getAddress(), contract, data, chain);
        return ((BigInteger) FunctionReturnDecoder.decode(call.getResult(), function.getOutputParameters()).get(0).getValue());
    }

    public static BigInteger estimateSet2(String contract, Credentials sender, ChainEnum chain) {
        List<Type> inputParameters = Lists.newArrayList();
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Uint256.class));
        Function function = new Function("estimateSet2", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(sender.getAddress(), contract, data, chain);
        return ((BigInteger) FunctionReturnDecoder.decode(call.getResult(), function.getOutputParameters()).get(0).getValue());
    }

    public static BigInteger estimateSet100(String contract, BigInteger gasLimit, Credentials sender, ChainEnum chain) {
        List<Type> inputParameters = Lists.newArrayList();
        List<TypeReference<?>> outputParameters = Lists.newArrayList();
        Function function = new Function("set", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createFunctionCallTransaction(sender.getAddress(), null, null, gasLimit, contract, data);
        return TransactionUtil.estimateGas(transaction, chain);
    }

    public static EthCall callSStore(String contract, BigInteger gasLimit, Credentials sender, ChainEnum chain) {
        List<Type> inputParameters = Lists.newArrayList();
        List<TypeReference<?>> outputParameters = Lists.newArrayList();
        Function function = new Function("set", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(sender.getAddress(), contract, gasLimit, data, chain);
        Web3ErrorUtil.throwChainError(call);
        return call;
    }

    public static String sStore(String contract, BigInteger gasLimit, Credentials sender, ChainEnum chain) {
        List<Type> inputParameters = Lists.newArrayList();
        List<TypeReference<?>> outputParameters = Lists.newArrayList();
        Function function = new Function("set", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        return TransactionUtil.execute(contract, BigInteger.ZERO, data, gasLimit, false, sender, chain).getTransactionHash();
    }

}
