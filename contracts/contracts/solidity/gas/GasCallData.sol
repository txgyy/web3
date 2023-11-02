// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract GasCallData {

    function test(bytes calldata b) external returns (uint256 gasCost) {
        uint256 preGas = gasleft();
        gasCost = preGas - gasleft();
    }

    function test() external returns (uint256 gasCost) {
        uint256 preGas = gasleft();
        gasCost = preGas - gasleft();
    }
}
