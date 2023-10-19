package xin.yukino.web3.contract.okc.aggregatelending;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import xin.yukino.web3.util.chain.ChainEnum;
import xin.yukino.web3.util.TransactionUtil;

import java.math.BigDecimal;

public class MockPriceOracleOKC {

    public static EthSendTransaction setAssetRatio(String contract,
                                                   String asset,
                                                   BigDecimal ratio,
                                                   Credentials sender,
                                                   ChainEnum chain) {
        Function function = new Function("setAssetRatio",
                Lists.newArrayList(new Address(asset), new Uint256(ratio.multiply(BigDecimal.TEN.pow(6)).toBigInteger())),
                Lists.newArrayList());
        String data = FunctionEncoder.encode(function);
        return TransactionUtil.execute(contract, data, sender, chain);
    }

}
