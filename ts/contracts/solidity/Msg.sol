// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.12;

contract Msg {

    error MsgData(
        bytes data,
        uint256 gas,
        address sender,
        bytes4 sig,
        uint256 value
    );

    fallback() external payable {
        revert MsgData(msg.data, gasleft(), msg.sender, msg.sig, msg.value);
    }
}
