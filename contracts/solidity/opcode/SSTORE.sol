// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract SSTORE {

    uint256 public a0;
    uint256 public a1;
    uint256 public a2;
    uint256 public a3;
    uint256 public a4;
    uint256 public a5;

    function estimateSet(uint256 gasLimit) public returns (uint256 b) {
        bytes memory data = abi.encodeWithSignature("set()");
        uint256 c = gasleft();
        (bool success,) = address(this).call{
                gas: gasLimit
            }(data);
        b = c - gasleft();
        require(success, "revert");
    }

    function estimateSet2() public returns (uint256 b) {
        uint256 c = gasleft();
        this.set();
        b = c - gasleft();
    }

    function set() external {
        a0 = 1;
        a1 = 1;
        a2 = 1;
        a3 = 1;
        a4 = 1;
        a5 = 1;
        reset();
    }

    function reset() public {
        a0 = 0;
        a1 = 0;
        a2 = 0;
        a3 = 0;
        a4 = 0;
        a5 = 0;
    }
}
