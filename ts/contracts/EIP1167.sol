// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;


contract EIP1167 {

    // 拷贝合约时，immutable、constant类型的变量会将值带入proxy合约中
    address public immutable _contractAddress;

    address private immutable _original;

    uint256 private count;

    constructor(address contractAddress){
        _contractAddress = contractAddress;
        _original = address(this);
    }

    function factory() external returns (address){
        count++;
        return clone(address(this));
    }

    /**
    * @dev Deploys and returns the address of a clone that mimics the behaviour of `implementation`.
     *
     * This function uses the create opcode, which should never revert.
     */
    function clone(address implementation) internal returns (address instance) {
        /// @solidity memory-safe-assembly
        assembly {
            let ptr := mload(0x40)
            mstore(ptr, 0x3d602d80600a3d3981f3363d3d373d3d3d363d73000000000000000000000000)
            mstore(add(ptr, 0x14), shl(0x60, implementation))
            mstore(add(ptr, 0x28), 0x5af43d82803e903d91602b57fd5bf30000000000000000000000000000000000)
        // 可以改为create2
            instance := create(0, ptr, 0x37)
        }
        require(instance != address(0), "ERC1167: create failed");
    }
}

