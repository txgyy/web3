package xin.yukino.web3.app.evm.eip.eip4337;

import lombok.Getter;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.generated.Uint256;

@Getter
public class StakeInfo extends StaticStruct {

    public Uint256 stake;

    public Uint256 unstakeDelaySec;

    public StakeInfo(Uint256 stake, Uint256 unstakeDelaySec) {
        super(stake, unstakeDelaySec);
        this.stake = stake;
        this.unstakeDelaySec = unstakeDelaySec;
    }
}
