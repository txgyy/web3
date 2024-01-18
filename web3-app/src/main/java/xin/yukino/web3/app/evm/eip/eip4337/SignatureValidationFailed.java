package xin.yukino.web3.app.evm.eip.eip4337;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import xin.yukino.web3.util.evm.CodecUtil;
import xin.yukino.web3.util.evm.error.ChainErrorMsg;
import xin.yukino.web3.util.evm.error.IEvmError;

import java.util.List;

@Getter
public class SignatureValidationFailed implements IEvmError {

    public static final Event ERROR = new Event("SignatureValidationFailed", Lists.newArrayList(TypeReference.create(Address.class)));
    public static final String ERROR_METHOD_ID = EventEncoder.encode(ERROR).substring(0, 10);

    private final Address aggregator;

    private final ChainErrorMsg error;


    public SignatureValidationFailed(ChainErrorMsg chainErrorMsg) {
        List<Type> types = CodecUtil.decodeError(chainErrorMsg.getHexData(), ERROR);
        this.aggregator = (Address) types.get(0);
        error = chainErrorMsg;
    }
}