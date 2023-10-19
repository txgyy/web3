package xin.yukino.web3.contract.xen;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import xin.yukino.web3.util.chain.ChainEnum;
import xin.yukino.web3.util.TransactionUtil;

import java.math.BigInteger;
import java.util.List;

public class XENFT {

    public static EthSendTransaction bulkClaimRank(int count, int term, String contractAddress, BigInteger gasLimit, Credentials credentials, ChainEnum chain) {
        List<Type> inputParameters = Lists.newArrayList(new Uint256(count), new Uint256(term));
        List<TypeReference<?>> outputParameters = Lists.newArrayList(new TypeReference<Uint256>() {
        });
        Function function = new Function("bulkClaimRank", inputParameters, outputParameters);

        return TransactionUtil.execute(contractAddress, FunctionEncoder.encode(function), credentials, chain);
    }

    public static EthSendTransaction bulkClaimMintReward(int tokenId, String contractAddress, Credentials credentials, ChainEnum chain) {
        List<Type> inputParameters = Lists.newArrayList(new Uint256(tokenId), new Address(credentials.getAddress()));
        List<TypeReference<?>> outputParameters = Lists.newArrayList();
        Function function = new Function("bulkClaimMintReward", inputParameters, outputParameters);

        return TransactionUtil.execute(contractAddress, FunctionEncoder.encode(function), credentials, chain);
    }

    public static EthCall tokenURI(int tokenId, String contractAddress, Credentials credentials, ChainEnum chain) {
        List<Type> inputParameters = Lists.newArrayList(new Uint256(tokenId));
        List<TypeReference<?>> outputParameters = Lists.newArrayList(new TypeReference<Utf8String>() {
        });
        Function function = new Function("tokenURI", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        return TransactionUtil.call(contractAddress, credentials.getAddress(), data, chain);
    }
}
