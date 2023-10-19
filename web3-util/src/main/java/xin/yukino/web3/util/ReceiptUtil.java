package xin.yukino.web3.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.*;
import xin.yukino.web3.util.chain.ChainEnum;
import xin.yukino.web3.util.chain.CommonTransactionReceipt;
import xin.yukino.web3.util.chain.arb.ArbTransactionReceipt;
import xin.yukino.web3.util.chain.op.OpTransactionReceipt;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ReceiptUtil {

    @SneakyThrows
    public static List<Log> getLogs(DefaultBlockParameter blockNumberStart, DefaultBlockParameter blockNumberEnd, List<String> addresses, ChainEnum chain, String... topics) {
        EthFilter ethFilter = new EthFilter(blockNumberStart, blockNumberEnd, addresses);
        ethFilter.addOptionalTopics(topics);
        EthLog ethLog = chain.getWeb3j().ethGetLogs(ethFilter).send();
        Web3ErrorUtil.throwChainError(ethLog);
        if (CollectionUtils.isEmpty(ethLog.getLogs())) {
            return Lists.newArrayList();
        }
        return ethLog.getLogs().stream().map(l -> ((EthLog.LogObject) l.get()).get()).collect(Collectors.toList());
    }

    public static List<Log> getLogs(long blockNumberStart, long blockNumberEnd, List<String> addresses, ChainEnum chain, String... topics) {
        return getLogs(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumberStart)), DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumberEnd)), addresses, chain, topics);
    }

    public static List<Log> getLogs(long blockNumber, List<String> addresses, ChainEnum chain, String... topics) {
        return getLogs(blockNumber, blockNumber, addresses, chain, topics);
    }

    public static List<Log> getLogs(long blockNumber, String address, ChainEnum chain, String... topics) {
        return getLogs(blockNumber, Collections.singletonList(address), chain, topics);
    }

    @SneakyThrows
    public static EthBlock.Block getBlock(DefaultBlockParameter defaultBlockParameter, ChainEnum chain) {
        EthBlock ethBlock = chain.getWeb3j().ethGetBlockByNumber(defaultBlockParameter, true).send();
        Web3ErrorUtil.throwChainError(ethBlock);
        return ethBlock.getBlock();
    }

    public static EthBlock.Block getBlock(long blockNumber, ChainEnum chain) {
        return getBlock(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)), chain);
    }

    @SneakyThrows
    public static Transaction getTransactionByHash(String txHash, ChainEnum chain) {
        EthTransaction ethTransaction = chain.getWeb3j().ethGetTransactionByHash(txHash).send();
        Web3ErrorUtil.throwChainError(ethTransaction);
        return ethTransaction.getTransaction().orElse(null);
    }

    @SneakyThrows
    public static CommonTransactionReceipt getTransactionReceipt(String txHash, ChainEnum chain) {
        EthGetTransactionReceipt transactionReceipt = chain.getWeb3j().ethGetTransactionReceipt(txHash).send();
        Web3ErrorUtil.throwChainError(transactionReceipt);

        JSONObject result = JSON.parseObject(transactionReceipt.getRawResponse()).getJSONObject("result");
        if (result == null) {
            return null;
        }

        CommonTransactionReceipt receipt;
        if (chain == ChainEnum.OP_MAIN || chain == ChainEnum.OP_TEST) {
            receipt = result.toJavaObject(OpTransactionReceipt.class);
        } else if (chain == ChainEnum.ARB_MAIN || chain == ChainEnum.ARB_TEST) {
            receipt = result.toJavaObject(ArbTransactionReceipt.class);
        } else {
            receipt = result.toJavaObject(CommonTransactionReceipt.class);
        }

        receipt.setRawResponse(result.toJSONString());
        return receipt;

    }
}
