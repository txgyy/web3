// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract Empty {

    event HandleSuccessExternalCalls();

    function a() external {
        b();
    }

    function b() internal {

    }
}
