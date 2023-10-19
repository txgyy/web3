package xin.yukino.web3.contract;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import xin.yukino.web3.util.chain.ChainEnum;
import xin.yukino.web3.util.TransactionUtil;

import java.math.BigDecimal;
import java.util.List;

public class Empty {

    public static EthSendTransaction transferEmpty(Credentials credentials, ChainEnum chain) {
        return TransactionUtil.transfer(credentials.getAddress(), BigDecimal.valueOf(0.0001), credentials, chain);
    }

    public static EthSendTransaction callEmpty(String contract, Credentials credentials, ChainEnum chain) {
        List<Type> inputParameters = Lists.newArrayList();
        List<TypeReference<?>> outputParameters = Lists.newArrayList();
        Function function = new Function("empty", inputParameters, outputParameters);

        return TransactionUtil.execute(contract, FunctionEncoder.encode(function), credentials, chain);
    }
}
