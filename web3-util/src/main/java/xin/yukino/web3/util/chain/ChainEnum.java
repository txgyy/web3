package xin.yukino.web3.util.chain;

import lombok.Getter;
import lombok.Setter;
import org.web3j.protocol.http.HttpService;
import xin.yukino.web3.util.constant.Web3Constant;
import xin.yukino.web3.util.web3j.Web3jDebug;

import java.math.BigInteger;
import java.net.Proxy;


@Getter
public enum ChainEnum {

    OKC_TEST(65, "OKTC Testnet", false, BigInteger.valueOf(3000_0000), "https://exchaintestrpc.okex.org"),

    OKC(66, "OKTC", false, BigInteger.valueOf(3000_0000), "https://exchainrpc.okex.org"),

    OKB_TEST(195, "OKBC Testnet", false, BigInteger.valueOf(3000_0000), "https://okbtestrpc.okbchain.org"),

    MATIC_TEST(80001, "Polygon Testnet", true, BigInteger.valueOf(2000_0000), "https://rpc.ankr.com/polygon_mumbai"),

    MATIC_MAIN(137, "Polygon", true, BigInteger.valueOf(2000_0000), "https://polygon-rpc.com"),

    BSC_TEST(97, "Bsc Testnet", false, BigInteger.valueOf(5000_0000), "https://data-seed-prebsc-1-s2.binance.org:8545"),

    BSC_MAIN(56, "Bsc", false, BigInteger.valueOf(5000_0000), "https://bsc-dataseed1.binance.org"),

    ARB_TEST(421613, "Arb Testnet", true, BigInteger.valueOf(5000_0000), "https://goerli-rollup.arbitrum.io/rpc"),

    ARB_MAIN(42161, "Arbitrum One", false, BigInteger.valueOf(5000_0000), "https://arb1.arbitrum.io/rpc"),

    AVAX_TEST(43113, "Avalanche Testnet", true, BigInteger.valueOf(1500_0000), "https://api.avax-test.network/ext/bc/C/rpc"),

    AVAX_MAIN(43114, "Avalanche", true, BigInteger.valueOf(1500_0000), "https://api.avax.network/ext/bc/C/rpc"),

    OP_TEST(420, "Optimism Testnet", true, BigInteger.valueOf(2500_0000), "https://goerli.optimism.io"),

    OP_MAIN(10, "Optimism", true, BigInteger.valueOf(2500_0000), "https://rpc.ankr.com/optimism"),

    ETH_TEST(5, "Ethereum Testnet", true, BigInteger.valueOf(1500_0000), "https://rpc.ankr.com/eth_goerli"),

    ETH_MAIN(1, "Ethereum", true, BigInteger.valueOf(1500_0000), "https://eth.llamarpc.com"),

    ;

    private final long chainId;

    private final String chainName;

    private final boolean eip1559;

    private final int decimal = Web3Constant.EVM_NATIVE_TOKEN_DECIMAL;

    private final BigInteger gasLimit;

    @Setter
    private String url;

    @Setter
    private boolean includeRawResponses;

    @Setter
    private HttpService web3jService;

    @Setter
    private Web3jDebug web3j;


    ChainEnum(long chainId, String chainName, boolean eip1559, BigInteger gasLimit, String url) {
        this.chainId = chainId;
        this.chainName = chainName;
        this.eip1559 = eip1559;
        this.gasLimit = gasLimit;
        this.url = url;

        this.web3jService = new HttpService(url, true);
        this.web3j = Web3jDebug.build(web3jService);
    }

    public ChainEnum setProxy(Proxy proxy) {
        this.web3jService = new HttpService(
                url, HttpService.getOkHttpClientBuilder().proxy(proxy)
                .build(),
                true);
        this.web3j = Web3jDebug.build(web3jService);
        return this;
    }

    public static ChainEnum resolveBy(long chainId) {
        for (ChainEnum chain : ChainEnum.values()) {
            if (chain.getChainId() == chainId) {
                return chain;
            }
        }

        return null;
    }
}
