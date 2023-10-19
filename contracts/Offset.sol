// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.17;

contract Offset {

    struct Struct {
        address a;
        uint256 b;
        bytes c;
        bytes d;
    }

    struct StructWithoutD {
        address a;
        uint256 b;
        bytes c;
    }

    function pack(Struct calldata s) public pure returns (bytes memory ret, uint256 ofs, uint256 sofs, uint256 len) {
        //lighter signature scheme. must match UserOp.ts#packUserOp
        bytes calldata d = s.d;
        // copy directly the userOp from calldata up to (but not including) the signature.
        // this encoding depends on the ABI encoding of calldata, but is much lighter to copy
        // than referencing each field separately.
        assembly {
            ofs := s
            sofs := d.offset
            len := sub(sub(sofs, ofs), 32)
            ret := mload(0x40)
            mstore(0x40, add(ret, add(len, 32)))
            mstore(ret, len)
            calldatacopy(add(ret, 32), ofs, len)
        }
    }

    function hash(Struct calldata s) external pure returns (bytes32) {
        (bytes memory ret, uint256 ofs, uint256 sofs, uint256 len) = pack(s);
        return keccak256(ret);
    }

    function hash(StructWithoutD calldata s) external pure returns (bytes32) {
        bytes memory ret;
        uint256 ofs;
        uint256 len;
        assembly {
            ofs := s
            ret := mload(0x40)
            len := ret
            mstore(0x40, add(ret, add(len, 32)))
            mstore(ret, len)
            calldatacopy(add(ret, 32), ofs, len)
        }
        return keccak256(ret);
    }
}
