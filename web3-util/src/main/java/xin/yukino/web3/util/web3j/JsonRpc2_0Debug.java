package xin.yukino.web3.util.web3j;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.JsonRpc2_0Admin;
import org.web3j.protocol.core.Request;
import xin.yukino.web3.util.web3j.config.TraceConfig;
import xin.yukino.web3.util.web3j.config.TracerTypeEnum;
import xin.yukino.web3.util.web3j.tracetransaction.calltracer.DebugCallTraceTransaction;

import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;

public class JsonRpc2_0Debug extends JsonRpc2_0Admin implements Web3jDebug {

    public JsonRpc2_0Debug(Web3jService web3jService) {
        super(web3jService);
    }

    public JsonRpc2_0Debug(
            Web3jService web3jService,
            long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        super(web3jService, pollingInterval, scheduledExecutorService);
    }

    @Override
    public Request<?, DebugCallTraceTransaction> debugCallTraceTransaction(String txHash, TraceConfig traceConfig) {
        traceConfig.setTracer(TracerTypeEnum.CALL_TRACER.getType());
        return new Request<>(
                "debug_traceTransaction",
                Arrays.asList(txHash, traceConfig),
                web3jService,
                DebugCallTraceTransaction.class);
    }
}
