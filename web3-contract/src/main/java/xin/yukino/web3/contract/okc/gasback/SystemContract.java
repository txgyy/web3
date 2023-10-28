package xin.yukino.web3.contract.okc.gasback;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import xin.yukino.web3.util.IChain;
import xin.yukino.web3.util.TransactionUtil;

import java.util.List;

public class SystemContract {

    public static EthSendTransaction invoke(String hex, String contractAddress, Credentials credentials, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList(new Utf8String(hex));
        List<TypeReference<?>> outputParameters = Lists.newArrayList();
        Function function = new Function("invoke", inputParameters, outputParameters);

        return TransactionUtil.execute(contractAddress, FunctionEncoder.encode(function), credentials, chain);
    }
}
