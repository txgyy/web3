package xin.yukino.web3.contract.okc.gasback;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthCall;
import xin.yukino.web3.util.chain.IChain;
import xin.yukino.web3.util.TransactionUtil;

import java.util.List;
import java.util.stream.Collectors;

public class GasBackMSGHelper {

    public static List<Type> genRegisterMsg(String contract, String withdraw, List<Integer> nonceList, String contractAddress, Credentials credentials, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList(new Address(contract), new Address(withdraw), new DynamicArray<>(Uint256.class, nonceList.stream().map(Uint256::new).collect(Collectors.toList())));
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Utf8String.class));
        Function function = new Function("genRegisterMsg", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        EthCall ethCall = TransactionUtil.call(credentials.getAddress(), contractAddress, data, chain);
        return FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
    }

    public static List<Type> genCancelMsg(String contract, String contractAddress, Credentials credentials, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList(new Address(contract));
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Utf8String.class));
        Function function = new Function("genCancelMsg", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        EthCall ethCall = TransactionUtil.call(credentials.getAddress(), contractAddress, data, chain);
        return FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
    }
}
