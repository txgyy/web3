package xin.yukino.web3.util.web3j;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.Request;
import xin.yukino.web3.util.web3j.tracetransaction.calltracer.DebugCallTraceTransaction;
import xin.yukino.web3.util.web3j.config.TraceConfig;

import java.util.concurrent.ScheduledExecutorService;

public interface Web3jDebug extends Admin {

    static JsonRpc2_0Debug build(Web3jService web3jService) {
        return new JsonRpc2_0Debug(web3jService);
    }

    static JsonRpc2_0Debug build(
            Web3jService web3jService,
            long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        return new JsonRpc2_0Debug(web3jService, pollingInterval, scheduledExecutorService);
    }

    Request<?, DebugCallTraceTransaction> debugCallTraceTransaction(String txHash, TraceConfig traceConfig);
}
