package xin.yukino.web3.util;

import lombok.SneakyThrows;
import xin.yukino.web3.util.chain.ChainEnum;
import xin.yukino.web3.util.web3j.config.TraceConfig;
import xin.yukino.web3.util.web3j.config.TracerConfig;
import xin.yukino.web3.util.web3j.tracetransaction.calltracer.CallTraceTransaction;
import xin.yukino.web3.util.web3j.tracetransaction.calltracer.DebugCallTraceTransaction;

public class Web3DebugUtil {

    @SneakyThrows
    public static CallTraceTransaction debugCallTraceTransaction(String txHash, boolean onlyTopCall, ChainEnum chain) {
        TraceConfig traceConfig = new TraceConfig();
        TracerConfig tracerConfig = new TracerConfig();
        tracerConfig.setOnlyTopCall(onlyTopCall);
        traceConfig.setTracerConfig(tracerConfig);
        DebugCallTraceTransaction debugCallTraceTransaction = chain.getWeb3j().debugCallTraceTransaction(txHash, traceConfig).send();
        Web3ErrorUtil.throwChainError(debugCallTraceTransaction);

        return debugCallTraceTransaction.getTraceTransaction().orElse(null);
    }
}
