package xin.yukino.web3.util;

import org.apache.commons.lang3.StringUtils;
import xin.yukino.web3.util.constant.Web3Constant;

import java.util.regex.Pattern;

/**
 * @author Yukino.Xin on 2023/10/28 14:52
 */
public class FieldUtil {

    private static final Pattern PATTERN = Pattern.compile("^0x[0-9a-f]*$");

    private static final Pattern PATTERN_ADDRESS = Pattern.compile("^0x[0-9a-fA-F]{40,40}$");

    public static boolean isValidAddress(Object value) {
        if (value == null) {
            return false;
        }

        if (!(value instanceof String)) {
            return false;
        }

        String value1 = (String) value;
        if (value1.length() < 2) {
            return false;
        }

        return PATTERN_ADDRESS.matcher(value1).matches();
    }

    public static boolean isValidHex(String value) {
        if (value == null) {
            return false;
        }

        if (value.length() < 3) {
            return false;
        }

        if (!value.startsWith(Web3Constant.HEX_PREFIX)) {
            return false;
        }

        return PATTERN.matcher(value).matches();
    }

    public static boolean isEmpty(String field) {
        return StringUtils.isEmpty(field) || Web3Constant.HEX_PREFIX.equals(field);
    }
}
