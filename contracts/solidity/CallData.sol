// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract CallData {

    struct A {
        bytes callData;
    }

    function abc(A calldata a) public view returns (A memory aa) {
        A memory aa = a;
    }
}
