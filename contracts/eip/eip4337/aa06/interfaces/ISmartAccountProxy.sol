// SPDX-License-Identifier: GPL-3.0
pragma solidity ^0.8.12;

interface ISmartAccountProxy {

    function masterCopy() external view returns (address);
}
