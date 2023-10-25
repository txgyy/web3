package xin.yukino.web3.contract.config;

import lombok.Getter;
import org.web3j.protocol.http.HttpService;
import xin.yukino.web3.util.chain.IChain;
import xin.yukino.web3.util.web3j.Web3jDebug;

/**
 * @author yukino.xin
 * @date 2023/10/25 23:19
 */

@Getter
public class Chain implements IChain {

    public static Chain OKC_TEST = new Chain(65, false, "https://exchaintestrpc.okex.org");

    public static Chain OKC_MAIN = new Chain(66, false, "https://exchainrpc.okex.org");

    public static Chain OKB_TEST = new Chain(195, false, "https://okbtestrpc.okbchain.org");

    public static Chain MATIC_TEST = new Chain(80001, true, "https://rpc.ankr.com/polygon_mumbai");

    public static Chain MATIC_MAIN = new Chain(137, true, "https://polygon-rpc.com");

    public static Chain BSC_TEST = new Chain(97, false, "https://data-seed-prebsc-1-s2.binance.org:8545");

    public static Chain BSC_MAIN = new Chain(56, false, "https://bsc-dataseed1.binance.org");

    public static Chain ARB_TEST = new Chain(421613, true, "https://goerli-rollup.arbitrum.io/rpc");

    public static Chain ARB_MAIN = new Chain(42161, false, "https://arb1.arbitrum.io/rpc");

    public static Chain AVAX_TEST = new Chain(43113, true, "https://api.avax-test.network/ext/bc/C/rpc");

    public static Chain AVAX_MAIN = new Chain(43114, true, "https://api.avax.network/ext/bc/C/rpc");

    public static Chain OP_TEST = new Chain(420, true, "https://goerli.optimism.io");

    public static Chain OP_MAIN = new Chain(10, true, "https://rpc.ankr.com/optimism");

    public static Chain ETH_TEST = new Chain(5, true, "https://rpc.ankr.com/eth_goerli");

    public static Chain ETH_MAIN = new Chain(1, true, "https://eth.llamarpc.com");


    private final long chainId;

    private final boolean eip1559;

    private Web3jDebug web3j;

    public Chain(long chainId, boolean eip1559, String rpc) {
        this.chainId = chainId;
        this.eip1559 = eip1559;

        HttpService web3jService = new HttpService(rpc, true);
        this.web3j = Web3jDebug.build(web3jService);
    }
}
