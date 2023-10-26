package xin.yukino.web3.contract.eip.eip4337;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import xin.yukino.web3.util.CodecUtil;
import xin.yukino.web3.util.error.ChainErrorMsg;
import xin.yukino.web3.util.error.IEvmError;

import java.util.List;

@Getter
public class FailedOp implements IEvmError {

    public static final Event ERROR = new Event("FailedOp", Lists.newArrayList(TypeReference.create(Uint256.class), TypeReference.create(Address.class), TypeReference.create(Utf8String.class)));

    public static final String ERROR_METHOD_ID = EventEncoder.encode(ERROR).substring(0, 10);

    private final Uint256 opIndex;

    private final Address paymaster;

    private final Utf8String reason;

    private final ChainErrorMsg error;


    public FailedOp(ChainErrorMsg chainErrorMsg) {
        List<Type> types = CodecUtil.decodeError(chainErrorMsg.getData(), ERROR);
        opIndex = (Uint256) types.get(0);
        paymaster = (Address) types.get(1);
        reason = (Utf8String) types.get(2);
        error = chainErrorMsg;
    }

}
