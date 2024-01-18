package xin.yukino.web3.app.evm.eip.eip4337;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserOpsPerAggregator extends DynamicStruct {

    private DynamicArray<UserOperation> userOps;

    private Address aggregator;

    private DynamicBytes signature;

    public UserOpsPerAggregator(DynamicArray<UserOperation> userOps, Address aggregator, DynamicBytes signature) {
        super(userOps, aggregator, signature);
        this.userOps = userOps;
        this.aggregator = aggregator;
        this.signature = signature;
    }
}
