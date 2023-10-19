package xin.yukino.web3.util;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ConvertUtil {

    public static BigDecimal toGWei(BigInteger n) {
        return Convert.fromWei(new BigDecimal(n), Convert.Unit.GWEI);
    }

    public static BigDecimal toEther(BigInteger n) {
        return Convert.fromWei(new BigDecimal(n), Convert.Unit.ETHER);
    }

    public static BigDecimal fromGWei(BigDecimal n) {
        return Convert.toWei(n, Convert.Unit.GWEI);
    }

    public static BigDecimal fromEther(BigDecimal n) {
        return Convert.toWei(n, Convert.Unit.ETHER);
    }

}
