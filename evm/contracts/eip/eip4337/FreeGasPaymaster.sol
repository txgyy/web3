// SPDX-License-Identifier: GPL-3.0
pragma solidity ^0.8.12;

import "@account-abstraction/contracts/core/BasePaymaster.sol";

contract FreeGasPaymaster is BasePaymaster {

    constructor(IEntryPoint _entryPoint) BasePaymaster(_entryPoint) {
        transferOwnership(address(0x5e4B00f651949e111D4e5f5d6c914aaaD28d40CF));
    }

    function _validatePaymasterUserOp(UserOperation calldata userOp, bytes32 userOpHash, uint256 maxCost)
    internal view override returns (bytes memory context, uint256 validationData){
        return ("", 0);

    }
}
