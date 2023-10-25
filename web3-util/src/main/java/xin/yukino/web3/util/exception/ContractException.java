package xin.yukino.web3.util.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xin.yukino.web3.util.error.EvmErrorMsg;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContractException extends RuntimeException {

    private final int code;

    private final String msg;

    private final String data;

    public ContractException(EvmErrorMsg error) {
        super(error.getCode() + "::" + error.getMessage() + (error.getData() == null ? "" : "::" + error.getData()));
        this.code = error.getCode();
        this.msg = error.getMessage();
        this.data = error.getData();
    }
}
