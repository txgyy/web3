package xin.yukino.web3.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.*;
import xin.yukino.web3.util.chain.IChain;
import xin.yukino.web3.util.constant.ChainIdConstant;
import xin.yukino.web3.util.web3j.resp.TransactionReceiptArb;
import xin.yukino.web3.util.web3j.resp.TransactionReceiptCommon;
import xin.yukino.web3.util.web3j.resp.TransactionReceiptOp;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ReceiptUtil {

    @SneakyThrows
    public static List<Log> getLogs(DefaultBlockParameter blockNumberStart, DefaultBlockParameter blockNumberEnd, List<String> addresses, IChain chain, String... topics) {
        EthFilter ethFilter = new EthFilter(blockNumberStart, blockNumberEnd, addresses);
        ethFilter.addOptionalTopics(topics);
        EthLog ethLog = chain.getWeb3j().ethGetLogs(ethFilter).send();
        ChainErrorUtil.throwChainError(ethLog);
        if (CollectionUtils.isEmpty(ethLog.getLogs())) {
            return Lists.newArrayList();
        }
        return ethLog.getLogs().stream().map(l -> ((EthLog.LogObject) l.get()).get()).collect(Collectors.toList());
    }

    public static List<Log> getLogs(long blockNumberStart, long blockNumberEnd, List<String> addresses, IChain chain, String... topics) {
        return getLogs(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumberStart)), DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumberEnd)), addresses, chain, topics);
    }

    public static List<Log> getLogs(long blockNumber, List<String> addresses, IChain chain, String... topics) {
        return getLogs(blockNumber, blockNumber, addresses, chain, topics);
    }

    public static List<Log> getLogs(long blockNumber, String address, IChain chain, String... topics) {
        return getLogs(blockNumber, Collections.singletonList(address), chain, topics);
    }

    @SneakyThrows
    public static EthBlock.Block getBlock(DefaultBlockParameter defaultBlockParameter, IChain chain) {
        EthBlock ethBlock = chain.getWeb3j().ethGetBlockByNumber(defaultBlockParameter, true).send();
        ChainErrorUtil.throwChainError(ethBlock);
        return ethBlock.getBlock();
    }

    public static EthBlock.Block getBlock(long blockNumber, IChain chain) {
        return getBlock(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)), chain);
    }

    @SneakyThrows
    public static Transaction getTransactionByHash(String txHash, IChain chain) {
        EthTransaction ethTransaction = chain.getWeb3j().ethGetTransactionByHash(txHash).send();
        ChainErrorUtil.throwChainError(ethTransaction);
        return ethTransaction.getTransaction().orElse(null);
    }

    @SneakyThrows
    public static TransactionReceipt getTransactionReceipt(String txHash, IChain chain) {
        EthGetTransactionReceipt transactionReceipt = chain.getWeb3j().ethGetTransactionReceipt(txHash).send();
        ChainErrorUtil.throwChainError(transactionReceipt);

        if (transactionReceipt.getRawResponse() == null) {
            return transactionReceipt.getTransactionReceipt().orElse(null);
        }
        JSONObject result = JSON.parseObject(transactionReceipt.getRawResponse()).getJSONObject("result");
        if (result == null) {
            return null;
        }

        TransactionReceiptCommon receipt;
        if (chain.getChainId() == ChainIdConstant.OP_MAIN || chain.getChainId() == ChainIdConstant.OP_TEST) {
            receipt = result.toJavaObject(TransactionReceiptOp.class);
        } else if (chain.getChainId() == ChainIdConstant.ARB_MAIN || chain.getChainId() == ChainIdConstant.ARB_TEST) {
            receipt = result.toJavaObject(TransactionReceiptArb.class);
        } else {
            receipt = result.toJavaObject(TransactionReceiptCommon.class);
        }

        receipt.setRawResponse(result.toJSONString());
        return receipt;

    }
}
