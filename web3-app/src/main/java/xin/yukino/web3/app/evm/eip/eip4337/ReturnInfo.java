package xin.yukino.web3.app.evm.eip.eip4337;

import lombok.Getter;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint48;

@Getter
public class ReturnInfo extends DynamicStruct {

    private final Uint256 preOpGas;

    private final Uint256 prefund;

    private final Bool sigFailed;

    private final Uint48 validAfter;

    private final Uint48 validUntil;

    private final DynamicBytes paymasterContext;

    public ReturnInfo(Uint256 preOpGas, Uint256 prefund, Bool sigFailed, Uint48 validAfter, Uint48 validUntil, DynamicBytes paymasterContext) {
        super(preOpGas, prefund, sigFailed, validAfter, validUntil, paymasterContext);
        this.preOpGas = preOpGas;
        this.prefund = prefund;
        this.sigFailed = sigFailed;
        this.validAfter = validAfter;
        this.validUntil = validUntil;
        this.paymasterContext = paymasterContext;
    }
}
