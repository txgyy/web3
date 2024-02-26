// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract Empty {

    uint256 private a = 0;
    uint256 private b = 0;

    function t() external {
        (bool success,) = msg.sender.call{value: 1}("");
        if (!success) {
            a = a + 1;
            b = a;
        }

    }

    function withdraw() external {
        (bool success,) = msg.sender.call{value: 1}("");
    }

    receive() external payable {
    }
}
