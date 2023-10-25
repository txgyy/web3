package xin.yukino.web3.util;


import com.google.common.collect.Iterables;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.RawTransactionManager;
import org.web3j.utils.Convert;
import xin.yukino.web3.util.chain.IChain;
import xin.yukino.web3.util.constant.Web3Constant;
import xin.yukino.web3.util.web3j.req.StateOverride;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TransactionUtil {

    //region execute
    @SneakyThrows
    public static EthSendTransaction sendTransaction(BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value, BigInteger nonce, Credentials credentials, IChain chain) {
        RawTransactionManager transactionManager = new RawTransactionManager(chain.getWeb3j(), credentials, chain.getChainId());
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);
        EthSendTransaction ethSendTransaction = transactionManager.signAndSend(rawTransaction);
        ChainErrorUtil.throwChainError(ethSendTransaction);
        return ethSendTransaction;
    }

    @SneakyThrows
    public static EthSendTransaction sendTransaction(BigInteger maxPriorityFeePerGas, BigInteger maxFeePerGas, BigInteger gasLimit, String to, String data, BigInteger value, BigInteger nonce, Credentials credentials, IChain chain) {
        RawTransactionManager transactionManager = new RawTransactionManager(chain.getWeb3j(), credentials, chain.getChainId());
        RawTransaction rawTransaction = RawTransaction.createTransaction(chain.getChainId(), nonce, gasLimit, to, value, data, maxPriorityFeePerGas, maxFeePerGas);
        EthSendTransaction ethSendTransaction = transactionManager.signAndSend(rawTransaction);
        ChainErrorUtil.throwChainError(ethSendTransaction);
        return ethSendTransaction;
    }

    public static EthSendTransaction execute(String to, BigInteger value, String data, BigInteger gasLimit,
                                             boolean isAccelerated, Credentials credentials,
                                             IChain chain, BigInteger... prices) {
        String from = credentials.getAddress();
        BigInteger nonce = AccountUtil.getNonce(from, isAccelerated, chain);
        if (chain.isEip1559()) {
            if (prices.length < 2) {
                ChainFee gasPrice1559 = get1559GasPrice(chain, Web3Constant.FEE_HISTORY_COMMON_REWARD_PERCENTILE);
                BigInteger maxPriorityFeePerGas = gasPrice1559.getMaxPriorityFeePerGas();
                BigInteger baseFee = gasPrice1559.getBaseFee();
                BigInteger maxFeePerGas = maxPriorityFeePerGas.add(baseFee);
                return sendTransaction(maxPriorityFeePerGas, maxFeePerGas, gasLimit, to, data, value, nonce, credentials, chain);
            } else {
                return sendTransaction(prices[1], prices[0], gasLimit, to, data, value, nonce, credentials, chain);
            }

        } else {
            if (prices.length < 1) {
                BigInteger gasPrice = getGasPrice(chain);
                return sendTransaction(gasPrice, gasLimit, to, data, value, nonce, credentials, chain);
            } else {
                return sendTransaction(prices[0], gasLimit, to, data, value, nonce, credentials, chain);

            }
        }
    }

    public static EthSendTransaction execute(String to,
                                             BigInteger value, String data, boolean isAccelerated, Credentials credentials,
                                             IChain chain, BigInteger... prices) {
        String from = credentials.getAddress();
        BigInteger gasLimit = estimateGas(from, to, data, chain);
        return execute(to, value, data, gasLimit.multiply(BigInteger.valueOf(2)), isAccelerated, credentials, chain, prices);
    }

    public static EthSendTransaction execute(String to, String data, Credentials credentials,
                                             IChain chain) {
        return execute(to, null, data, false, credentials, chain);
    }

    public static EthSendTransaction transfer(String to, BigDecimal value, Credentials credentials, IChain chain) {
        return execute(to, Convert.toWei(value, Convert.Unit.ETHER).toBigIntegerExact(), "", false, credentials, chain);
    }

    //endregion

    //region call
    @SneakyThrows
    public static EthCall call(String from, BigInteger nonce, BigInteger gasLimit, String to,
                               BigInteger value, String data, IChain chain, BigInteger blockNumber, StateOverride stateOverride, BigInteger... prices) {
        Transaction transaction;
        if (prices.length == 0) {
            transaction = Transaction.createFunctionCallTransaction(from, nonce, null, gasLimit, to, value, data);
        } else if (chain.isEip1559()) {
            transaction = new Transaction(from, nonce, null, gasLimit, to, value, data, chain.getChainId(), prices[1], prices[0]);
        } else {
            transaction = Transaction.createFunctionCallTransaction(from, nonce, prices[0], gasLimit, to, value, data);
        }

        DefaultBlockParameter defaultBlockParameter;
        if (blockNumber == null) {
            defaultBlockParameter = DefaultBlockParameterName.LATEST;
        } else {
            defaultBlockParameter = DefaultBlockParameter.valueOf(blockNumber);
        }
        return new Request<>(
                "eth_call",
                Arrays.asList(transaction, defaultBlockParameter, stateOverride),
                chain.getWeb3j().getWeb3jService(),
                EthCall.class).send();
    }

    public static EthCall call(String from, String to, BigInteger gasLimit, String function, IChain chain) {
        return call(from, null, gasLimit, to, null, function, chain, null, null);
    }

    public static EthCall call(String from, String to, String function, IChain chain) {
        return call(from, null, null, to, null, function, chain, null, null);
    }

    public static EthCall call(String from, String to, String function, IChain chain, StateOverride stateOverride) {
        return call(from, null, null, to, null, function, chain, null, stateOverride);
    }
    //endregion

    //region estimateGas
    @SneakyThrows
    public static BigInteger estimateGas(Transaction transaction, IChain chain) {
        EthEstimateGas ethEstimateGas = chain.getWeb3j().ethEstimateGas(transaction).send();
        ChainErrorUtil.throwEvmError(ethEstimateGas);
        return ethEstimateGas.getAmountUsed();
    }

    public static BigInteger estimateGas(String from, String to, String data, IChain chain) {
        Transaction transaction = Transaction.createEthCallTransaction(from, to, data);
        return estimateGas(transaction, chain);
    }
    //endregion

    @SneakyThrows
    public static BigInteger getGasPrice(IChain chain) {
        EthGasPrice ethGasPrice = chain.getWeb3j().ethGasPrice().send();
        ChainErrorUtil.throwChainError(ethGasPrice);
        return ethGasPrice.getGasPrice();
    }

    @SneakyThrows
    public static BigInteger getMaxPriorityFeePerGas(IChain chain) {
        EthMaxPriorityFeePerGas ethMaxPriorityFeePerGas = chain.getWeb3j().ethMaxPriorityFeePerGas().send();
        ChainErrorUtil.throwChainError(ethMaxPriorityFeePerGas);
        return ethMaxPriorityFeePerGas.getMaxPriorityFeePerGas();
    }

    @SneakyThrows
    public static ChainFee get1559GasPrice(IChain chain, Double rewardPercentile) {
        EthFeeHistory ethFeeHistory = chain.getWeb3j().ethFeeHistory(2, DefaultBlockParameterName.PENDING, Collections.singletonList(rewardPercentile)).send();
        ChainErrorUtil.throwChainError(ethFeeHistory);
        EthFeeHistory.FeeHistory feeHistory = ethFeeHistory.getFeeHistory();
        BigInteger lastBaseFee = Iterables.getLast(feeHistory.getBaseFeePerGas());
        List<BigInteger> rewards = Iterables.getLast(feeHistory.getReward());
        return new ChainFee(lastBaseFee, Iterables.getLast(rewards));
    }

    @Data
    @AllArgsConstructor
    public static class ChainFee {

        private final BigInteger baseFee;

        private final BigInteger maxPriorityFeePerGas;
    }

}
