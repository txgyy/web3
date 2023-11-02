// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract StateOverrideTest {

    uint256 public constant number = 2;

    uint256 public n;

    function getNumber() external view returns (uint256){
        return number;
    }

    function setN(uint256 _n) external {
        n = _n;
    }
}
