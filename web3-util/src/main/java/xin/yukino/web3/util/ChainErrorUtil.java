package xin.yukino.web3.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.core.Response;
import xin.yukino.web3.util.constant.Web3Constant;
import xin.yukino.web3.util.error.EvmError;
import xin.yukino.web3.util.error.EvmErrorMsg;
import xin.yukino.web3.util.exception.ChainException;
import xin.yukino.web3.util.exception.ContractException;

import java.util.List;

@Slf4j
public class ChainErrorUtil {

    public static void throwEvmError(Response response) {
        EvmErrorMsg evmErrorMsg = parseEvmError(response);
        if (evmErrorMsg == EvmErrorMsg.DEFAULT) {
            return;
        }
        throw new ContractException(evmErrorMsg);
    }

    public static EvmErrorMsg parseEvmError(Response response) {
        Object result = response.getResult();
        if (result instanceof String && ((String) result).startsWith(EvmError.ERROR_METHOD_ID)) {
            String hexRevertReason = ((String) result).substring(EvmError.ERROR_METHOD_ID.length());
            List<Type> decoded = FunctionReturnDecoder.decode(hexRevertReason, EvmError.ERROR.getParameters());
            String decodedRevertReason = ((Utf8String) decoded.get(0)).getValue();
            return new EvmErrorMsg(0, decodedRevertReason, (String) result);
        }
        if (!response.hasError()) {
            return EvmErrorMsg.DEFAULT;
        }
        Response.Error error = response.getError();
        String message = StringUtils.strip(error.getMessage(), "\"");
        String data = StringUtils.strip(error.getData(), "\"");
        if (message.startsWith(Web3Constant.HEX_PREFIX)) {
            data = message;
        }
        return new EvmErrorMsg(error.getCode(), message, data);
    }

    public static void throwChainError(Response response) {
        if (!response.hasError()) {
            return;
        }

        throw new ChainException(response.getError());
    }
}
