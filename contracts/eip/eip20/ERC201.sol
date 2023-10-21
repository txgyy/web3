// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";

contract ERC201 is ERC20("ERC201", "ERC201") {
    constructor() {}

    function mint(address account, uint256 amount) public {
        _mint(account, amount);
    }
}
