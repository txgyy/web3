package xin.yukino.web3.app.evm.eip.eip4337;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint48;
import xin.yukino.web3.util.evm.CodecUtil;
import xin.yukino.web3.util.evm.error.ChainErrorMsg;
import xin.yukino.web3.util.evm.error.IEvmError;

import java.util.List;

@Getter
public class ExecutionResult implements IEvmError {

    public static final Event ERROR = new Event("ExecutionResult",
            Lists.newArrayList(
                    TypeReference.create(Uint256.class), TypeReference.create(Uint256.class),
                    TypeReference.create(Uint48.class), TypeReference.create(Uint48.class),
                    TypeReference.create(Bool.class), TypeReference.create(DynamicBytes.class)));

    public static final String ERROR_METHOD_ID = EventEncoder.encode(ERROR).substring(0, 10);

    private final Uint256 preOpGas;

    private final Uint256 paid;

    private final Uint48 validAfter;

    private final Uint48 validUntil;

    private final Bool targetSuccess;

    private final DynamicBytes targetResult;

    private final ChainErrorMsg error;


    public ExecutionResult(ChainErrorMsg chainErrorMsg) {
        List<Type> types = CodecUtil.decodeError(chainErrorMsg.getHexData(), ERROR);
        this.preOpGas = (Uint256) types.get(0);
        this.paid = (Uint256) types.get(1);
        this.validAfter = (Uint48) types.get(2);
        this.validUntil = (Uint48) types.get(3);
        this.targetSuccess = (Bool) types.get(4);
        this.targetResult = (DynamicBytes) types.get(5);
        error = chainErrorMsg;
    }
}
