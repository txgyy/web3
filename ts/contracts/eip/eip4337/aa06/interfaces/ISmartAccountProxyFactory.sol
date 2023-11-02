// SPDX-License-Identifier: GPL-3.0
pragma solidity ^0.8.12;

interface ISmartAccountProxyFactory {

    function safeSingleton(address singleton) external view returns (bool);
}
