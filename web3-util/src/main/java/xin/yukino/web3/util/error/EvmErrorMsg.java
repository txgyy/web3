package xin.yukino.web3.util.error;

import lombok.Getter;
import xin.yukino.web3.util.CodecUtil;
import xin.yukino.web3.util.constant.Web3Constant;

/**
 * go-ethereum@v1.10.8-okc1/core/vm/errors.go
 */
@Getter
public class EvmErrorMsg {

    public static final EvmErrorMsg DEFAULT = new EvmErrorMsg(Integer.MIN_VALUE, "", "", Web3Constant.HEX_PREFIX);

    private final int code;

    private final String reason;

    private final String hexData;

    public EvmErrorMsg(int code, String message, String data, String hexData) {
        this.code = code;
        this.reason = message + "::" + data;
        if (CodecUtil.isValidHex(hexData)) {
            this.hexData = hexData;
        } else {
            this.hexData = Web3Constant.HEX_PREFIX;
        }
    }

    public EvmErrorMsg(String hexData) {
        this.code = 0;
        this.reason = "";
        this.hexData = hexData;
    }

    public String getMethodId() {
        if (CodecUtil.isEmpty(hexData)) {
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
