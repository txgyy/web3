package xin.yukino.web3.util.error;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.datatypes.Event;
import xin.yukino.web3.util.constant.Web3Constant;

public interface IEvmError {

    ChainErrorMsg getError();

    default String getMethodId() {
        return getError().getMethodId();
    }

    static String getMethodId(Event ERROR) {
        return EventEncoder.encode(ERROR).substring(0, Web3Constant.METHOD_ID_LENGTH);
    }

    static IEvmError parseDefaultError(ChainErrorMsg chainErrorMsg) {
        if (chainErrorMsg.isMethodId(EvmError.ERROR_METHOD_ID)) {
            return new EvmError(chainErrorMsg);
        } else {
            return new UnKnowError(chainErrorMsg);
        }
    }

    static String resolveFinalReason(String reason) {
        if (reason.startsWith(EvmError.ERROR_METHOD_ID)) {
            return new EvmError(reason).getReason();
        } else {
            return reason;
        }
    }
}
