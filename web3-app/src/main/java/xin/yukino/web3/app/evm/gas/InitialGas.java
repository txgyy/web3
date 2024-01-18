package xin.yukino.web3.app.evm.gas;

import org.web3j.utils.Numeric;
import xin.yukino.web3.app.evm.constant.GasConstant;

public class InitialGas {

    public static long initialGas(String callData) {
        byte[] callDataBytes = Numeric.hexStringToByteArray(callData);
        long callDataGasUsed = 0;
        for (byte b : callDataBytes) {
            callDataGasUsed += b == (byte) 0 ? GasConstant.ZERO_BYTE : GasConstant.NON_ZERO_BYTE;
        }

        return GasConstant.FIX_TX_GAS + callDataGasUsed;
    }


}
