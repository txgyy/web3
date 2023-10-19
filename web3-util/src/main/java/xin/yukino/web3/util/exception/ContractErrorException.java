package xin.yukino.web3.util.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xin.yukino.web3.util.error.EvmErrorMsg;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContractErrorException extends RuntimeException {

    private final int code;

    private final String msg;

    private final String data;

    public ContractErrorException(EvmErrorMsg error) {
        super(error.getReason());
        this.code = error.getCode();
        this.msg = error.getReason();
        this.data = error.getHexData();
    }
}
