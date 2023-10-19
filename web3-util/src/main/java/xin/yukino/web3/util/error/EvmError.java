package xin.yukino.web3.util.error;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.utils.Numeric;
import xin.yukino.web3.util.CodecUtil;

import java.util.List;

@Getter
public class EvmError implements IEvmError {

    public static final Event ERROR = new Event("Error", Lists.newArrayList(TypeReference.create(Utf8String.class)));

    // 0x08c379a0
    public static final String ERROR_METHOD_ID = IEvmError.getMethodId(ERROR);

    private final String hexData;

    private final String reason;

    private final EvmErrorMsg error;

    public EvmError(EvmErrorMsg evmErrorMsg) {
        List<Type> types = CodecUtil.decodeError(evmErrorMsg.getHexData(), ERROR);
        String reason = (String) types.get(0).getValue();
        String hexData;
        if (reason.startsWith(ERROR_METHOD_ID)) {
            hexData = reason;
        } else {
            hexData = Numeric.toHexString(reason.getBytes());
        }

        error = evmErrorMsg;
        if (hexData.startsWith(ERROR_METHOD_ID)) {
            evmErrorMsg = new EvmErrorMsg(hexData);
            this.hexData = evmErrorMsg.getHexData();
            this.reason = new EvmError(evmErrorMsg).getReason();
        } else {
            this.hexData = hexData;
            this.reason = reason;
        }
    }

    public EvmError(String hexData) {
        this(new EvmErrorMsg(hexData));
    }

}
