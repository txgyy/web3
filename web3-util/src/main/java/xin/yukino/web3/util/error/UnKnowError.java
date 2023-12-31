package xin.yukino.web3.util.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnKnowError implements IEvmError {

    private final String message;

    private final EvmErrorMsg error;

    public UnKnowError(EvmErrorMsg error) {
        this.message = error.getReason();
        this.error = error;
    }
}
