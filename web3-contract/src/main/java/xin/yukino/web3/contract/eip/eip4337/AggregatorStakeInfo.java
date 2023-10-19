package xin.yukino.web3.contract.eip.eip4337;

import lombok.Getter;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.StaticStruct;

@Getter
public class AggregatorStakeInfo extends StaticStruct {

    public final Address actualAggregator;

    public final StakeInfo stakeInfo;

    public AggregatorStakeInfo(Address actualAggregator, StakeInfo stakeInfo) {
        super(actualAggregator, stakeInfo);
        this.actualAggregator = actualAggregator;
        this.stakeInfo = stakeInfo;
    }
}
