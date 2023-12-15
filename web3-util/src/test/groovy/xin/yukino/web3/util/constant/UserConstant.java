package xin.yukino.web3.util.constant;

import org.web3j.crypto.Credentials;
import xin.yukino.web3.util.WalletUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author yukino.xin
 * @date 2023/11/7 10:23
 */
public class UserConstant {

    private static final Properties ENV = new Properties();

    static {
        try {
            ENV.load(Files.newInputStream(Paths.get(System.getProperty("user.dir"), "..", ".env")));
        } catch (IOException ignored) {
        }
    }

    public static final Credentials CREDENTIALS = WalletUtil.generateBip44Credentials(ENV.getProperty("DEFAULT_MNEMONIC"), 0);

    public static final Credentials DEX = Credentials.create(ENV.getProperty("PRIVATE_KEY"));

}
