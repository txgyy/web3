package xin.yukino.web3.util.evm;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.web3j.protocol.core.Response;
import xin.yukino.web3.util.evm.constant.Web3Constant;
import xin.yukino.web3.util.evm.error.ChainErrorMsg;
import xin.yukino.web3.util.evm.error.EvmError;
import xin.yukino.web3.util.evm.exception.ChainException;

@Slf4j
public class ChainErrorUtil {

    public static ChainErrorMsg parseChainError(Response response) {
        Object result = response.getResult();
        if (result instanceof String && ((String) result).startsWith(EvmError.ERROR_METHOD_ID)) {
            return new ChainErrorMsg(0, new EvmError((String) result).getReason(), (String) result);
        }
        if (!response.hasError()) {
            return ChainErrorMsg.DEFAULT;
        }
        Response.Error error = response.getError();
        String message = error.getMessage();
        String data = StringUtils.strip(error.getData(), "\"");
        if (!StringUtils.startsWith(data, Web3Constant.HEX_PREFIX)
                && StringUtils.startsWith(message, Web3Constant.HEX_PREFIX)) {
            data = message;
        }
        return new ChainErrorMsg(error.getCode(), message, data);
    }

    public static void throwChainError(Response response) {
        if (!response.hasError()) {
            return;
        }

        throw new ChainException(response.getError());
    }
}
