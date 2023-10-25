package xin.yukino.web3.contract.eip.eip4337;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import xin.yukino.web3.util.CodecUtil;
import xin.yukino.web3.util.error.EvmErrorMsg;
import xin.yukino.web3.util.error.IEvmError;

import java.util.List;

@Getter
@AllArgsConstructor
public class ValidationResult implements IEvmError {

    public static final Event ERROR = new Event("ValidationResult", Lists.newArrayList(TypeReference.create(ReturnInfo.class), TypeReference.create(StakeInfo.class), TypeReference.create(StakeInfo.class), TypeReference.create(StakeInfo.class)));

    public static final String ERROR_METHOD_ID = EventEncoder.encode(ERROR).substring(0, 10);
    private final ReturnInfo returnInfo;

    private final StakeInfo senderInfo;

    private final StakeInfo factoryInfo;

    private final StakeInfo paymasterInfo;

    private final EvmErrorMsg error;

    public ValidationResult(EvmErrorMsg evmErrorMsg) {
        List<Type> types = CodecUtil.decodeError(evmErrorMsg.getData(), ERROR);
        this.returnInfo = (ReturnInfo) types.get(0);
        this.senderInfo = (StakeInfo) types.get(1);
        this.factoryInfo = (StakeInfo) types.get(2);
        this.paymasterInfo = (StakeInfo) types.get(3);
        error = evmErrorMsg;
    }
}

