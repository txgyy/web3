// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract Revert {

    error SenderAddressResult(address sender);

    function r() public {
        revert SenderAddressResult(address(this));
    }
}
