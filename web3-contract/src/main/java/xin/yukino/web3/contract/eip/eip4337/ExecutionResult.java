package xin.yukino.web3.contract.eip.eip4337;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import xin.yukino.web3.util.CodecUtil;
import xin.yukino.web3.util.error.EvmErrorMsg;
import xin.yukino.web3.util.error.IEvmError;

import java.util.List;

@Getter
public class ExecutionResult implements IEvmError {

    public static final Event ERROR = new Event("ExecutionResult",
            Lists.newArrayList(
                    TypeReference.create(Uint256.class), TypeReference.create(Uint256.class),
                    TypeReference.create(Uint256.class), TypeReference.create(Uint256.class)));

    public static final String ERROR_METHOD_ID = EventEncoder.encode(ERROR).substring(0, 10);

    private final Uint256 preOpGas;

    private final Uint256 paid;

    private final Uint256 deadline;

    private final Uint256 paymasterDeadline;

    private final EvmErrorMsg error;


    public ExecutionResult(EvmErrorMsg evmErrorMsg) {
        List<Type> types = CodecUtil.decodeError(evmErrorMsg.getData(), ERROR);
        this.preOpGas = (Uint256) types.get(0);
        this.paid = (Uint256) types.get(1);
        this.deadline = (Uint256) types.get(2);
        this.paymasterDeadline = (Uint256) types.get(3);
        error = evmErrorMsg;
    }
}
