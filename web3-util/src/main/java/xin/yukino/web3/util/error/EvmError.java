package xin.yukino.web3.util.error;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.utils.Numeric;
import xin.yukino.web3.util.CodecUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;


@Getter
public class EvmError implements IEvmError {

    public static final Event ERROR = new Event("Error", Lists.newArrayList(TypeReference.create(Utf8String.class)));

    public static final Event ERROR_BYTES = new Event("Error", Lists.newArrayList(TypeReference.create(DynamicBytes.class)));

    // 0x08c379a0
    public static final String ERROR_METHOD_ID = IEvmError.getMethodId(ERROR);

    private final ChainErrorMsg error;

    private final String reason;

    public EvmError(ChainErrorMsg evmErrorMsg) {
        String hexData = evmErrorMsg.getHexData();
        if (hexData.startsWith(ERROR_METHOD_ID)) {
            List<Type> types = CodecUtil.decodeError(hexData, ERROR_BYTES);
            hexData = Numeric.toHexString(((DynamicBytes) types.get(0)).getValue());
            this.reason = new EvmError(hexData).getReason();
        } else if ((hexData.length() - 10) % 64 != 0 || !hexData.contains("000")) {
            byte[] bytes = Numeric.hexStringToByteArray(evmErrorMsg.getHexData());
            this.reason = new String(bytes, StandardCharsets.UTF_8);
        } else {
            this.reason = hexData;
        }
        error = evmErrorMsg;
    }

    public EvmError(String hexData) {
        this(new ChainErrorMsg(hexData));
    }


}
