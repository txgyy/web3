// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract ChainId {

    function chainId() public returns (uint256) {
        return block.chainid;
    }
}
