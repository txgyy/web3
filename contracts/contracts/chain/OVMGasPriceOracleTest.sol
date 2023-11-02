// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract OVMGasPriceOracleTest {

    address public singleton = 0x420000000000000000000000000000000000000F;

    fallback() external payable {
        // solhint-disable-next-line no-inline-assembly
        assembly {
            let _singleton := and(
                sload(0),
                0xffffffffffffffffffffffffffffffffffffffff
            )
            calldatacopy(0, 0, calldatasize())
            let success := delegatecall(
                gas(),
                _singleton,
                0,
                calldatasize(),
                0,
                0
            )
            returndatacopy(0, 0, returndatasize())
            if eq(success, 0) {
                revert(0, returndatasize())
            }
            return (0, returndatasize())
        }
    }
}
