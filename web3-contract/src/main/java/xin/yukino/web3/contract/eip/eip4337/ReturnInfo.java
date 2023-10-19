package xin.yukino.web3.contract.eip.eip4337;

import lombok.Getter;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.generated.Uint256;

@Getter
public class ReturnInfo extends DynamicStruct {

    private final Uint256 preOpGas;

    private final Uint256 prefund;

    private final Uint256 deadline;

    private final Uint256 paymasterDeadline;

    private final DynamicBytes paymasterContext;

    public ReturnInfo(Uint256 preOpGas, Uint256 prefund, Uint256 deadline, Uint256 paymasterDeadline, DynamicBytes paymasterContext) {
        super(preOpGas, prefund, deadline, paymasterDeadline, paymasterContext);
        this.preOpGas = preOpGas;
        this.prefund = prefund;
        this.deadline = deadline;
        this.paymasterDeadline = paymasterDeadline;
        this.paymasterContext = paymasterContext;
    }
}
