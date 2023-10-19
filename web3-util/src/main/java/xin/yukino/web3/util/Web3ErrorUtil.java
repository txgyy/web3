package xin.yukino.web3.util;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.core.Response;
import xin.yukino.web3.util.constant.Web3Constant;
import xin.yukino.web3.util.error.EvmError;
import xin.yukino.web3.util.error.EvmErrorMsg;
import xin.yukino.web3.util.exception.ChainErrorException;
import xin.yukino.web3.util.exception.ContractErrorException;

import java.util.List;

@Slf4j
public class Web3ErrorUtil {

    public static void throwEvmError(Response response) {
        EvmErrorMsg evmErrorMsg = parseEvmError(response);
        if (evmErrorMsg == EvmErrorMsg.DEFAULT) {
            return;
        }
        throw new ContractErrorException(evmErrorMsg);
    }

    public static EvmErrorMsg parseEvmError(Response response) {
        Object result = response.getResult();
        if (result instanceof String && ((String) result).startsWith(EvmError.ERROR_METHOD_ID)) {
            String hexRevertReason = ((String) result).substring(EvmError.ERROR_METHOD_ID.length());
            List<Type> decoded = FunctionReturnDecoder.decode(hexRevertReason, EvmError.ERROR.getParameters());
            String decodedRevertReason = ((Utf8String) decoded.get(0)).getValue();
            return new EvmErrorMsg(0, decodedRevertReason, "", (String) result);
        }
        if (!response.hasError()) {
            return EvmErrorMsg.DEFAULT;
        }
        Response.Error error = response.getError();
        String message = StringUtils.isEmpty(error.getMessage()) ? "" : StringUtils.strip(error.getMessage(), "\"");
        String data = StringUtils.isEmpty(error.getData()) ? "" : StringUtils.strip(error.getData(), "\"");
        String hexData = Web3Constant.HEX_PREFIX;
        if (message.startsWith(Web3Constant.HEX_PREFIX)) {
            hexData = message;
        } else if (data.startsWith(Web3Constant.HEX_PREFIX)) {
            hexData = data;
        } else if (message.lastIndexOf("]") != -1) {
            String reason = message.substring(0, message.lastIndexOf("]") + 1);
            List<String> array = JSON.parseArray(reason, String.class);
            hexData = array.get(3);
        } else if (data.lastIndexOf("]") != -1) {
            String reason = data.substring(0, data.lastIndexOf("]") + 1);
            List<String> array = JSON.parseArray(reason, String.class);
            hexData = array.get(3);
        }
        return new EvmErrorMsg(error.getCode(), error.getMessage(), error.getData(), hexData);
    }

    public static void throwChainError(Response response) {
        if (!response.hasError()) {
            return;
        }

        throw new ChainErrorException(response.getError());
    }
}
