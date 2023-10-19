// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

import "../interfaces/ISmartAccountProxy.sol";
import "../interfaces/ISmartAccountProxyFactory.sol";

contract AccountUtil {

    function validateWalletWhitelist(address sender) internal {
        require(sender.code.length > 0, "sender is not aa account");

        bytes32 runtimeCodeHashV1 = bytes32(0x1cf5a0fe3bf24282cc81b8ffae2e5e3aa750f3f182a6665e22ba4ab24da3aaf3);
        bytes32 runtimeCodeHashV2 = bytes32(0x01a99ad3717020cbba98fc5b74f0d921d6f56d622bc7a15c0dac860582d3cb96);
        bool isCodeEqual = sender.codehash == runtimeCodeHashV1 || sender.codehash == runtimeCodeHashV2;
        require(isCodeEqual, "sender proxy not in whitelist");

        bool isSingletonEqual = ISmartAccountProxyFactory(address(0x5bE4C3d1742A81bbD568ac1A06b275a0a6D6E6e8))
            .safeSingleton(ISmartAccountProxy(sender).masterCopy());
        require(isSingletonEqual, "sender implement not in whitelist");
    }
}
