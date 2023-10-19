package xin.yukino.web3.util.error;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.datatypes.Event;
import xin.yukino.web3.util.constant.Web3Constant;

public interface IEvmError {

    EvmErrorMsg getError();

    default String getMethodId() {
        return getError().getMethodId();
    }

    static String getMethodId(Event ERROR) {
        return EventEncoder.encode(ERROR).substring(0, Web3Constant.METHOD_ID_LENGTH);
    }

    static IEvmError parseDefaultError(EvmErrorMsg evmErrorMsg) {
        if (evmErrorMsg.isMethodId(EvmError.ERROR_METHOD_ID)) {
            return new EvmError(evmErrorMsg);
        } else {
            return new UnKnowError(evmErrorMsg);
        }
    }
}
