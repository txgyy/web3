package xin.yukino.web3.contract.eip.eip20;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.tx.Contract;
import xin.yukino.web3.util.event.IEvent;

import java.math.BigInteger;
import java.util.List;

@Getter
public class Transfer implements IEvent {

    public static final Event EVENT = new Event("Transfer", Lists.newArrayList(TypeReference.create(Address.class, true), TypeReference.create(Address.class, true), TypeReference.create(Uint256.class)));

    public static final String EVENT_SIG = EventEncoder.encode(EVENT);

    private final String from;
    private final String to;
    private final BigInteger amount;

    private final Log log;

    public Transfer(Log log) {
        EventValues eventValues = Contract.staticExtractEventParameters(EVENT, log);
        List<Type> indexedValues = eventValues.getIndexedValues();
        List<Type> nonIndexedValues = eventValues.getNonIndexedValues();
        from = (String) indexedValues.get(0).getValue();
        to = (String) indexedValues.get(1).getValue();
        amount = (BigInteger) nonIndexedValues.get(0).getValue();
        this.log = log;
    }
}

