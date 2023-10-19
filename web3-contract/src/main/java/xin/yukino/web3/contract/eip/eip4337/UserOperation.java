package xin.yukino.web3.contract.eip.eip4337;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.generated.Uint256;


@Data
@EqualsAndHashCode(callSuper = false)
public class UserOperation extends DynamicStruct {

    private Address sender;

    private Uint256 nonce;

    private DynamicBytes initCode;

    private DynamicBytes callData;

    private Uint256 callGasLimit;

    private Uint256 verificationGasLimit;

    private Uint256 preVerificationGas;

    private Uint256 maxFeePerGas;

    private Uint256 maxPriorityFeePerGas;

    private DynamicBytes paymasterAndData;

    private DynamicBytes signature;

    public UserOperation(Address sender, Uint256 nonce, DynamicBytes initCode, DynamicBytes callData, Uint256 callGasLimit, Uint256 verificationGasLimit, Uint256 preVerificationGas, Uint256 maxFeePerGas, Uint256 maxPriorityFeePerGas, DynamicBytes paymasterAndData, DynamicBytes signature) {
        super(sender, nonce, initCode, callData, callGasLimit, verificationGasLimit, preVerificationGas, maxFeePerGas, maxPriorityFeePerGas, paymasterAndData, signature);
        this.sender = sender;
        this.nonce = nonce;
        this.initCode = initCode;
        this.callData = callData;
        this.callGasLimit = callGasLimit;
        this.verificationGasLimit = verificationGasLimit;
        this.preVerificationGas = preVerificationGas;
        this.maxFeePerGas = maxFeePerGas;
        this.maxPriorityFeePerGas = maxPriorityFeePerGas;
        this.paymasterAndData = paymasterAndData;
        this.signature = signature;
    }
}
