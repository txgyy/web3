package xin.yukino.web3.util.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.web3j.protocol.core.Response;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChainErrorException extends RuntimeException {

    private final int code;

    private final String msg;

    private final String data;

    public ChainErrorException(Response.Error error) {
        super(error.getCode() + "::" + error.getMessage() + "::" + error.getData());
        this.code = error.getCode();
        this.msg = error.getMessage();
        this.data = error.getData();
    }
}
