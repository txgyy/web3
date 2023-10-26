package xin.yukino.web3.contract.eip.eip4337;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import xin.yukino.web3.util.CodecUtil;
import xin.yukino.web3.util.error.ChainErrorMsg;
import xin.yukino.web3.util.error.IEvmError;

import java.util.List;

@Getter
public class SenderAddressResult implements IEvmError {

    public static final Event ERROR = new Event("SenderAddressResult", Lists.newArrayList(TypeReference.create(Address.class)));
    public static final String ERROR_METHOD_ID = EventEncoder.encode(ERROR).substring(0, 10);

    private final Address address;

    private final ChainErrorMsg error;


    public SenderAddressResult(ChainErrorMsg chainErrorMsg) {
        List<Type> types = CodecUtil.decodeError(chainErrorMsg.getData(), ERROR);
        this.address = (Address) types.get(0);
        error = chainErrorMsg;
    }
}
