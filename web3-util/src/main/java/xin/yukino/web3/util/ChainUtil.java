package xin.yukino.web3.util;

import lombok.SneakyThrows;
import org.web3j.protocol.admin.methods.response.TxPoolContent;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthChainId;
import org.web3j.protocol.core.methods.response.NetVersion;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import xin.yukino.web3.util.chain.ChainEnum;

import java.math.BigInteger;

public class ChainUtil {

    //region getBlockNumber getNonce getGasPrice getNetVersion getChainId

    @SneakyThrows
    public static BigInteger getBlockNumber(ChainEnum chain) {
        EthBlockNumber ethBlockNumber = chain.getWeb3j().ethBlockNumber().send();
        Web3ErrorUtil.throwChainError(ethBlockNumber);
        return ethBlockNumber.getBlockNumber();
    }

    @SneakyThrows
    public static String getNetVersion(ChainEnum chain) {
        NetVersion netVersion = chain.getWeb3j().netVersion().send();
        Web3ErrorUtil.throwChainError(netVersion);
        return netVersion.getNetVersion();
    }

    @SneakyThrows
    public static BigInteger getChainId(ChainEnum chain) {
        EthChainId ethChainId = chain.getWeb3j().ethChainId().send();
        Web3ErrorUtil.throwChainError(ethChainId);
        return ethChainId.getChainId();
    }

    @SneakyThrows
    public static String getWeb3ClientVersion(ChainEnum chain) {
        Web3ClientVersion web3ClientVersion = chain.getWeb3j().web3ClientVersion().send();
        Web3ErrorUtil.throwChainError(web3ClientVersion);
        return web3ClientVersion.getWeb3ClientVersion();
    }
    //endregion


    @SneakyThrows
    public static TxPoolContent.TxPoolContentResult getTxPoolContent(ChainEnum chain) {
        TxPoolContent txPoolContent = chain.getWeb3j().txPoolContent().send();
        Web3ErrorUtil.throwChainError(txPoolContent);
        return txPoolContent.getResult();
    }

}
