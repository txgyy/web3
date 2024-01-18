package xin.yukino.web3.app.evm.eip.eip4337;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Numeric;
import xin.yukino.web3.util.evm.CodecUtil;
import xin.yukino.web3.util.evm.error.ChainErrorMsg;
import xin.yukino.web3.util.evm.error.EvmError;
import xin.yukino.web3.util.evm.error.IEvmError;

import java.util.List;

@Getter
public class FailedOp implements IEvmError {

    public static final Event ERROR_V6 = new Event("FailedOp", Lists.newArrayList(TypeReference.create(Uint256.class), TypeReference.create(Utf8String.class)));

    public static final Event ERROR_BYTES_V6 = new Event("FailedOp", Lists.newArrayList(TypeReference.create(Uint256.class), TypeReference.create(DynamicBytes.class)));

    // 0x220266b6
    public static final String ERROR_METHOD_ID_V6 = EventEncoder.encode(ERROR_V6).substring(0, 10);

    public static final Event ERROR_OKX = new Event("FailedOp", Lists.newArrayList(TypeReference.create(Uint256.class), TypeReference.create(Address.class), TypeReference.create(Utf8String.class)));

    public static final Event ERROR_BYTES_OKX = new Event("FailedOp", Lists.newArrayList(TypeReference.create(Uint256.class), TypeReference.create(Address.class), TypeReference.create(DynamicBytes.class)));

    // 0x00fa072b
    public static final String ERROR_METHOD_ID_OKX = EventEncoder.encode(ERROR_OKX).substring(0, 10);

    private final int opIndex;

    private final String reason;

    private final ChainErrorMsg error;

    public FailedOp(ChainErrorMsg evmErrorMsg) {
        List<Type> types;
        byte[] bytes;
        if (evmErrorMsg.isMethodId(ERROR_METHOD_ID_V6)) {
            types = CodecUtil.decodeError(evmErrorMsg.getHexData(), ERROR_BYTES_V6);
            bytes = ((DynamicBytes) types.get(1)).getValue();
        } else {
            types = CodecUtil.decodeError(evmErrorMsg.getHexData(), ERROR_BYTES_OKX);
            bytes = ((DynamicBytes) types.get(2)).getValue();
        }

        opIndex = ((Uint256) types.get(0)).getValue().intValue();

        String hexData = Numeric.toHexString(bytes);
        reason = new EvmError(hexData).getReason();
        error = evmErrorMsg;
    }


    public FailedOp(String hexData) {
        this(new ChainErrorMsg(hexData));
    }

    public static boolean isMatch(String methodId) {
        return methodId.equals(ERROR_METHOD_ID_V6) || methodId.equals(ERROR_METHOD_ID_OKX);
    }

}
