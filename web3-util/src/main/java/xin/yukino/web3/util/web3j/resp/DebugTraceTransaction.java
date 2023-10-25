package xin.yukino.web3.util.web3j.resp;

import org.web3j.protocol.core.Response;

import java.util.Map;
import java.util.Optional;

public class DebugTraceTransaction extends Response<Map<String, Object>> {

    public Optional<Map<String, Object>> getTraceTransaction() {
        return Optional.ofNullable(getResult());
    }

}
