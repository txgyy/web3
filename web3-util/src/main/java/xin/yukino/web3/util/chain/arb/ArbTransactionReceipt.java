package xin.yukino.web3.util.chain.arb;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.web3j.utils.Numeric;
import xin.yukino.web3.util.chain.CommonTransactionReceipt;

import java.math.BigInteger;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArbTransactionReceipt extends CommonTransactionReceipt {

    private String gasUsedForL1;

    private String l1BlockNumber;

    public BigInteger resolveGasUsedForL1() {
        return Numeric.decodeQuantity(gasUsedForL1);
    }

    public BigInteger resolveL1BlockNumber() {
        return Numeric.decodeQuantity(l1BlockNumber);
    }

}
