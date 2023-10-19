package xin.yukino.web3.contract.eip.eip4337.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.protocol.core.methods.response.EthCall;
import xin.yukino.web3.util.chain.ChainEnum;
import xin.yukino.web3.util.TransactionUtil;
import xin.yukino.web3.util.Web3ErrorUtil;

import java.util.Map;

public class AccountUtil {

    public static void validateWalletWhitelist(String sender, ChainEnum chain) {
        Function function = new Function("validateWalletWhitelist", Lists.newArrayList(new Address(sender)),
                Lists.newArrayList());
        String data = FunctionEncoder.encode(function);
        String contract = "0x5ff137d4b0fdcd49dca30c7cf57e578a026d2789";
        Map<String, Map<String, String>> stateOverride = Maps.newHashMap();
        Map<String, String> codeState = Maps.newHashMap();
        codeState.put("code", "0x608060405234801561001057600080fd5b506004361061002b5760003560e01c80639fcd740e14610030575b600080fd5b61004361003e3660046102bc565b610045565b005b7f1cf5a0fe3bf24282cc81b8ffae2e5e3aa750f3f182a6665e22ba4ab24da3aaf37f01a99ad3717020cbba98fc5b74f0d921d6f56d622bc7a15c0dac860582d3cb96600073ffffffffffffffffffffffffffffffffffffffff84163f8314806100c45750818473ffffffffffffffffffffffffffffffffffffffff163f145b90506000735be4c3d1742a81bbd568ac1a06b275a0a6d6e6e873ffffffffffffffffffffffffffffffffffffffff16635eb43cb58673ffffffffffffffffffffffffffffffffffffffff1663a619486e6040518163ffffffff1660e01b8152600401602060405180830381865afa158015610143573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061016791906102e0565b6040517fffffffff0000000000000000000000000000000000000000000000000000000060e084901b16815273ffffffffffffffffffffffffffffffffffffffff9091166004820152602401602060405180830381865afa1580156101d0573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906101f491906102fd565b90508180156102005750805b610290576040517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152602160248201527f73656e64657220696d706c656d656e74206e6f7420696e2077686974656c697360448201527f7400000000000000000000000000000000000000000000000000000000000000606482015260840160405180910390fd5b5050505050565b73ffffffffffffffffffffffffffffffffffffffff811681146102b957600080fd5b50565b6000602082840312156102ce57600080fd5b81356102d981610297565b9392505050565b6000602082840312156102f257600080fd5b81516102d981610297565b60006020828403121561030f57600080fd5b815180151581146102d957600080fdfea26469706673582212204723db212f38bac39e0aae1f4d3452436162a8de5c6e3b8fb29d2fa1ed0ad6de64736f6c63430008110033");
        stateOverride.put(contract, codeState);
        EthCall call = TransactionUtil.call(contract, data, chain, stateOverride);
        Web3ErrorUtil.throwChainError(call);
    }
}
