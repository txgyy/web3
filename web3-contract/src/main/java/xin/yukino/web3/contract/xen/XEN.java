package xin.yukino.web3.contract.xen;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import xin.yukino.web3.util.IChain;
import xin.yukino.web3.util.TransactionUtil;

import java.util.List;

public class XEN {

    public static EthSendTransaction claimRank(int term, String contractAddress, Credentials credentials, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList(new Uint256(term));
        List<TypeReference<?>> outputParameters = Lists.newArrayList();
        Function function = new Function("claimRank", inputParameters, outputParameters);
        return TransactionUtil.execute(contractAddress, FunctionEncoder.encode(function), credentials, chain);
    }

    public static EthSendTransaction claimMintReward(String contractAddress, Credentials credentials, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList();
        List<TypeReference<?>> outputParameters = Lists.newArrayList();
        Function function = new Function("claimMintReward", inputParameters, outputParameters);
        return TransactionUtil.execute(contractAddress, FunctionEncoder.encode(function), credentials, chain);
    }
}
