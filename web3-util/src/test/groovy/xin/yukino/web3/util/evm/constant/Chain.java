package xin.yukino.web3.util.evm.constant;

import lombok.Getter;
import org.web3j.crypto.transaction.type.TransactionType;
import org.web3j.protocol.http.HttpService;
import xin.yukino.web3.util.evm.IChain;
import xin.yukino.web3.util.evm.web3j.Web3jDebug;

/**
 * @author yukino.xin
 * @date 2023/10/25 23:19
 */

@Getter
public class Chain implements IChain {

    public static Chain OKC_TEST = new Chain(65, TransactionType.LEGACY, "https://exchaintestrpc.okex.org");

    public static Chain OKC_MAIN = new Chain(66, TransactionType.LEGACY, "https://fullnode.okg.com/okchain/fork/aawallet/rpc");

    public static Chain MATIC_TEST = new Chain(80001, TransactionType.EIP1559, "https://rpc.ankr.com/polygon_mumbai");

    public static Chain MATIC_MAIN = new Chain(137, TransactionType.EIP1559, "https://fullnode.okg.com/api/poly/fork/analysis/rpc");

    public static Chain BSC_TEST = new Chain(97, TransactionType.LEGACY, "https://data-seed-prebsc-1-s2.binance.org:8545");

    public static Chain BSC_MAIN = new Chain(56, TransactionType.LEGACY, "https://bsc-dataseed1.binance.org");

    public static Chain ARB_TEST = new Chain(421613, TransactionType.LEGACY, "https://goerli-rollup.arbitrum.io/rpc");

    public static Chain ARB_MAIN = new Chain(42161, TransactionType.LEGACY, "https://arb1.arbitrum.io/rpc");

    public static Chain AVAX_TEST = new Chain(43113, TransactionType.LEGACY, "https://api.avax-test.network/ext/bc/C/rpc");

    public static Chain AVAX_MAIN = new Chain(43114, TransactionType.LEGACY, "https://api.avax.network/ext/bc/C/rpc");

    public static Chain OP_TEST = new Chain(420, TransactionType.EIP1559, "https://goerli.optimism.io");

    public static Chain OP_MAIN = new Chain(10, TransactionType.EIP1559, "https://rpc.ankr.com/optimism");

    public static Chain ETH_TEST = new Chain(5, TransactionType.EIP1559, "https://rpc.ankr.com/eth_goerli");

    public static Chain ETH_MAIN = new Chain(1, TransactionType.EIP1559, "https://eth.llamarpc.com");

    public static Chain LOCAL = new Chain(1337, TransactionType.EIP1559, "http://127.0.0.1:8545");

    private final long chainId;

    private final TransactionType txType;

    private Web3jDebug web3j;

    public Chain(long chainId, TransactionType txType, String rpc) {
        this.chainId = chainId;
        this.txType = txType;

        HttpService web3jService = new HttpService(rpc, true);
        this.web3j = Web3jDebug.build(web3jService);
    }
}
