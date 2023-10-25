package xin.yukino.web3.util.web3j.resp;

import org.web3j.protocol.core.Response;

import java.util.Map;
import java.util.Optional;

public class DebugTraceCall extends Response<Map<String, Object>> {

    public Optional<Map<String, Object>> getTraceCall() {
        return Optional.ofNullable(getResult());
    }
}
