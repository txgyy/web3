package xin.yukino.web3.contract.solidity.opcode;


import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.EthCall;
import xin.yukino.web3.util.chain.ChainEnum;
import xin.yukino.web3.util.TransactionUtil;

import java.util.List;

/**
 * // SPDX-License-Identifier: MIT
 * pragma solidity ^0.8.0;
 * contract Empty {
 * function m()
 * public
 * pure
 * returns (
 * uint256 a,
 * uint256 b,
 * uint256 c,
 * uint256 d,
 * uint256 e,
 * uint256 f,
 * uint256 g,
 * uint256 h
 * )
 * {
 * assembly {
 * a := mload(0x0)
 * b := mload(0x40)
 * mstore(0x0, b)
 * mstore(
 * 0x40,
 * 0xff
 * )
 * c := mload(0x60)
 * d := mload(0x80)
 * e := mload(0x40)
 * f := mload(0x0)
 * mstore(
 * mload(0x40),
 * 0xffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff
 * )
 * g := mload(0x40)
 * h := mload(mload(0x40))
 * }
 * }
 * }
 */
public class Mload {

    public static EthCall mload(String contract, String from, ChainEnum chain) {
        List<Type> inputParameters = Lists.newArrayList();
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Uint256.class), TypeReference.create(Uint256.class), TypeReference.create(Uint256.class), TypeReference.create(Uint256.class), TypeReference.create(Uint256.class), TypeReference.create(Uint256.class), TypeReference.create(Uint256.class), TypeReference.create(Uint256.class));
        Function function = new Function("m", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(from, contract, data, chain);
        return call;
    }
}
