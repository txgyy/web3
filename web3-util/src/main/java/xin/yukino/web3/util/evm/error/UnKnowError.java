package xin.yukino.web3.util.evm.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnKnowError implements IEvmError {

    private final String message;

    private final ChainErrorMsg error;

    public UnKnowError(ChainErrorMsg error) {
        this.message = error.getMessage() + (error.getHexData() == null ? "" : "::" + error.getHexData());
        this.error = error;
    }
}