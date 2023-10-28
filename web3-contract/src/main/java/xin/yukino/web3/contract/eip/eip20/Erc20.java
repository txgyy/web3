package xin.yukino.web3.contract.eip.eip20;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import xin.yukino.web3.util.IChain;
import xin.yukino.web3.util.ChainErrorUtil;
import xin.yukino.web3.util.TransactionUtil;

import java.math.BigInteger;
import java.util.List;

public class Erc20 {

    public static String name(String contract, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList();
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Utf8String.class));
        Function function = new Function("name", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(contract, contract, data, chain);
        return (String) FunctionReturnDecoder.decode(call.getValue(), function.getOutputParameters()).get(0).getValue();
    }

    public static String symbol(String contract, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList();
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Utf8String.class));
        Function function = new Function("symbol", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(contract, contract, data, chain);
        return (String) FunctionReturnDecoder.decode(call.getValue(), function.getOutputParameters()).get(0).getValue();
    }

    public static BigInteger decimals(String contract, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList();
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Uint8.class));
        Function function = new Function("decimals", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(contract, contract, data, chain);
        ChainErrorUtil.throwChainError(call);
        return (BigInteger) FunctionReturnDecoder.decode(call.getValue(), function.getOutputParameters()).get(0).getValue();
    }

    public static BigInteger balanceOf(String contract, String from, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList(new Address(from));
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Uint256.class));
        Function function = new Function("balanceOf", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(from, contract, data, chain);
        return (BigInteger) FunctionReturnDecoder.decode(call.getValue(), function.getOutputParameters()).get(0).getValue();
    }

    public static EthSendTransaction mint(String contract, String account, BigInteger amount, Credentials credentials, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList(new Address(account), new Uint256(amount));
        List<TypeReference<?>> outputParameters = Lists.newArrayList();
        Function function = new Function("mint", inputParameters, outputParameters);
        return TransactionUtil.execute(contract, FunctionEncoder.encode(function), credentials, chain);
    }

    public static EthSendTransaction transferFrom(String contract, String from, String to, BigInteger amount, Credentials credentials, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList(new Address(from), new Address(to), new Uint256(amount));
        List<TypeReference<?>> outputParameters = Lists.newArrayList();
        Function function = new Function("transferFrom", inputParameters, outputParameters);
        return TransactionUtil.execute(contract, FunctionEncoder.encode(function), credentials, chain);
    }


    public static EthSendTransaction transfer(String contract, String to, BigInteger amount, Credentials credentials, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList(new Address(to), new Uint256(amount));
        List<TypeReference<?>> outputParameters = Lists.newArrayList();
        Function function = new Function("transfer", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        return TransactionUtil.execute(contract, data, credentials, chain);
    }

    public static EthSendTransaction approve(String contract, String to, BigInteger amount, Credentials credentials, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList(new Address(to), new Uint256(amount));
        List<TypeReference<?>> outputParameters = Lists.newArrayList();
        Function function = new Function("approve", inputParameters, outputParameters);
        return TransactionUtil.execute(contract, FunctionEncoder.encode(function), credentials, chain);
    }

}
