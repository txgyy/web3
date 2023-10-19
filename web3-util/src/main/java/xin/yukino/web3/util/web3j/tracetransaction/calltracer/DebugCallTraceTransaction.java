package xin.yukino.web3.util.web3j.tracetransaction.calltracer;

import org.web3j.protocol.core.Response;

import java.util.Optional;

public class DebugCallTraceTransaction extends Response<CallTraceTransaction> {

    public Optional<CallTraceTransaction> getTraceTransaction() {
        return Optional.ofNullable(getResult());
    }

}
