package xin.yukino.web3.app.evm.solidity.msg;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.tx.Contract;
import xin.yukino.web3.util.evm.event.IEvent;

import java.util.List;

@Getter
public class MsgData implements IEvent {

    public static final Event ERROR = new Event("MsgData", Lists.newArrayList(
            TypeReference.create(DynamicBytes.class), TypeReference.create(Uint256.class),
            TypeReference.create(Address.class), TypeReference.create(Bytes4.class),
            TypeReference.create(Uint256.class))
    );

    public static final String EVENT_SIG = EventEncoder.encode(ERROR).substring(0, 10);

    private final DynamicBytes data;
    private final Uint256 gas;
    private final Address sender;
    private final Bytes4 signature;
    private final Uint256 value;
    private final Log log;

    public MsgData(Log log) {
        EventValues eventValues = Contract.staticExtractEventParameters(ERROR, log);
        List<Type> indexedValues = eventValues.getIndexedValues();
        List<Type> nonIndexedValues = eventValues.getNonIndexedValues();
        data = (DynamicBytes) nonIndexedValues.get(0);
        gas = (Uint256) nonIndexedValues.get(1);
        sender = (Address) nonIndexedValues.get(2);
        signature = (Bytes4) nonIndexedValues.get(3);
        value = (Uint256) nonIndexedValues.get(4);
        this.log = log;
    }

}
