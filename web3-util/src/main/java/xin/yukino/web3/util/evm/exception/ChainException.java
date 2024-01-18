package xin.yukino.web3.util.evm.exception;

import lombok.Getter;
import org.web3j.protocol.core.Response;

@Getter
public class ChainException extends RuntimeException {

    private final int code;

    private final String msg;

    private final String data;

    public ChainException(Response.Error error) {
        super(error.getCode() + "::" + error.getMessage() + (error.getData() == null ? "" : "::" + error.getData()));
        this.code = error.getCode();
        this.msg = error.getMessage();
        this.data = error.getData();
    }
}

