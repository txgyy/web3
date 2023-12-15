package xin.yukino.web3.util.error;

import lombok.Getter;
import xin.yukino.web3.util.CodecUtil;
import xin.yukino.web3.util.FieldUtil;
import xin.yukino.web3.util.constant.Web3Constant;

/**
 * go-ethereum@v1.10.8-okc1/core/vm/errors.go
 */
@Getter
public class ChainErrorMsg {

    public static final ChainErrorMsg DEFAULT = new ChainErrorMsg(Integer.MIN_VALUE, "", Web3Constant.HEX_PREFIX);

    private final int code;

    private final String message;

    private final String hexData;

    public ChainErrorMsg(int code, String message, String hexData) {
        this.code = code;
        this.message = message;
        if (FieldUtil.isValidHex(hexData)) {
            this.hexData = hexData;
        } else {
            this.hexData = Web3Constant.HEX_PREFIX;
        }
    }

    public ChainErrorMsg(String hexData) {
        this.code = 0;
        this.message = hexData;
        this.hexData = hexData;
    }

    public String getMethodId() {
        if (FieldUtil.isEmpty(hexData)) {
            return Web3Constant.HEX_PREFIX;
        }

        if (CodecUtil.hasValidMethodId(hexData)) {
            return hexData.substring(0, Web3Constant.METHOD_ID_LENGTH);
        } else {
            return Web3Constant.HEX_PREFIX;
        }

    }

    public boolean isMethodId(String methodId) {
        return getMethodId().equals(methodId);
    }
}
