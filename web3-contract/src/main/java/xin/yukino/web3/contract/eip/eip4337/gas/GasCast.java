package xin.yukino.web3.contract.eip.eip4337.gas;

import com.google.common.collect.Lists;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;

public class GasCast {

    public static final Event EVENT = new Event("GasCast", Lists.newArrayList(
            TypeReference.create(Uint256.class, true)));

    public static final String EVENT_SIG = EventEncoder.encode(EVENT);


}
