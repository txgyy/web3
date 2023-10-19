package xin.yukino.web3.contract.okc.aggregatelending;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import xin.yukino.web3.util.chain.ChainEnum;
import xin.yukino.web3.util.TransactionUtil;

import java.math.BigInteger;

public class Router {

    public static EthSendTransaction supply(String contract, String account, String asset,
                                            BigInteger amount, Boolean collateralAble,
                                            Credentials sender,
                                            ChainEnum chain) {
        UserAssetParams assetParams = new UserAssetParams(new Address(asset), new Uint256(amount), new Address(account));
        Function function = new Function("supply",
                Lists.newArrayList(assetParams, new Bool(collateralAble), new Bool(true)),
                Lists.newArrayList());
        String data = FunctionEncoder.encode(function);
        return TransactionUtil.execute(contract, data, sender, chain);
    }

    public static EthSendTransaction borrow(String contract, String asset,
                                            BigInteger amount,
                                            Credentials sender,
                                            ChainEnum chain) {
        UserAssetParams assetParams = new UserAssetParams(new Address(asset), new Uint256(amount), new Address(sender.getAddress()));
        Function function = new Function("borrow",
                Lists.newArrayList(assetParams, new Bool(true)),
                Lists.newArrayList());
        String data = FunctionEncoder.encode(function);
        return TransactionUtil.execute(contract, data, sender, chain);
    }

    public static EthSendTransaction repay(String contract, String asset,
                                           BigInteger amount,
                                           Credentials sender,
                                           ChainEnum chain) {
        UserAssetParams assetParams = new UserAssetParams(new Address(asset), new Uint256(amount), new Address(sender.getAddress()));
        Function function = new Function("repay",
                Lists.newArrayList(assetParams, new Bool(true)),
                Lists.newArrayList());
        String data = FunctionEncoder.encode(function);
        return TransactionUtil.execute(contract, data, sender, chain);
    }
}
