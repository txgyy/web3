package xin.yukino.web3.util.chain.op;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.web3j.utils.Numeric;
import xin.yukino.web3.util.chain.CommonTransactionReceipt;

import java.math.BigInteger;

@EqualsAndHashCode(callSuper = true)
@Data
public class OpTransactionReceipt extends CommonTransactionReceipt {

    private String l1Fee;

    private String l1FeeScalar;

    private String l1GasPrice;

    private String l1GasUsed;

    public BigInteger resolveL1Fee() {
        return Numeric.decodeQuantity(l1Fee);
    }

    public BigInteger resolveL1FeeScalar() {
        return Numeric.decodeQuantity(l1FeeScalar);
    }

    public BigInteger resolveL1GasPrice() {
        return Numeric.decodeQuantity(l1GasPrice);
    }

    public BigInteger resolveL1GasUsed() {
        return Numeric.decodeQuantity(l1GasUsed);
    }

}
