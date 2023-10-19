// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract Balances {

    mapping(address => uint256) private _balances;

    function balances(address addr) external returns (uint256) {
        return _balances[addr];
    }
}
