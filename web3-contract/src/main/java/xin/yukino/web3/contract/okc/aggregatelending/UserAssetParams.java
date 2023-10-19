package xin.yukino.web3.contract.okc.aggregatelending;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.generated.Uint256;

public class UserAssetParams extends StaticStruct {

    public final Address asset;

    public final Uint256 amount;

    public final Address to;

    public UserAssetParams(Address asset, Uint256 amount, Address to) {
        super(asset, amount, to);
        this.asset = asset;
        this.amount = amount;
        this.to = to;
    }

}
