package xin.yukino.web3.contract.eip.eip4337.entrypoint

import com.esaulpaugh.headlong.abi.Function
import com.esaulpaugh.headlong.abi.Tuple
import org.web3j.abi.TypeEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.DynamicStruct
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.utils.Numeric
import spock.lang.Specification
import xin.yukino.web3.contract.config.Chain
import xin.yukino.web3.contract.constant.UserConstant
import xin.yukino.web3.contract.eip.eip4337.FailedOp
import xin.yukino.web3.util.ChainErrorUtil
import xin.yukino.web3.util.CodecUtil
import xin.yukino.web3.util.TransactionUtil
import xin.yukino.web3.util.WalletUtil
import xin.yukino.web3.util.error.ChainErrorMsg

class EntrypointTest extends Specification {
    def "HandleOps"() {
        given:
        def chain = Chain.MATIC_MAIN
        def credentials = UserConstant.CREDENTIALS
        def entrypoint = "0x5ff137d4b0fdcd49dca30c7cf57e578a026d2789"
        def paymaster = "0xEFE9118a8A61b71B1a71e76aaf593ad755A2F3A1"
        def gasPrice = TransactionUtil.get1559GasPrice(chain, 50D)
        def senders = [
                "0x1627995e3d27c937c6e8520845d3ba43a2c553d3",
                "0x1b409bdadb49fe02d8342afc43bb8451976bb360",
                "0x0c19998ade137743f6aeafc37950d499ff48f1ce",
                "0xb28e242e0fd240fd02ea62050ff09d990552421c",
                "0xcf2fca36463e4c77f60e1f8924a39e8f2f0a0b2a",
                "0xfab1c5a4b728b894549d5557537a666206c29eb2",
                "0x51a2d1b88eac4bc20328c0c7e54af1792de9e8ab",
                "0x61b573de3070b0af3a516ae048820f6cfb6d6824",
                "0xf6ee766d1a786c78c8c3764297b56911b6ddd444",
                "0x0086a0c3fdc85d5e79b66fe93ef9c53099b340ab"
        ]

        def initCode = new byte[0]


        def func = Function.parse("transfer(address,uint256)").encodeCallWithArgs(com.esaulpaugh.headlong.abi.Address.wrap(CodecUtil.toChecksumAddress(credentials.getAddress())), BigInteger.ONE).array()
        def callData = Function.parse("execute(address,uint256,bytes)").encodeCallWithArgs(com.esaulpaugh.headlong.abi.Address.wrap(CodecUtil.toChecksumAddress("0x3c499c542cef5e3811e1192ce70d8cc03d5c3359")), BigInteger.ZERO, func).array()
        def callGasLimit = BigInteger.TEN.pow(5)
        def verificationGasLimit = BigInteger.TEN.pow(5)
        def preVerificationGas = BigInteger.ZERO
        def maxFeePerGas = gasPrice.getBaseFee().multiply(BigInteger.valueOf(15)).divide(BigInteger.TEN) + gasPrice.getMaxPriorityFeePerGas()
        def maxPriorityFeePerGas = gasPrice.getMaxPriorityFeePerGas()
        def paymasterAndData = Numeric.hexStringToByteArray(paymaster);

        byte[] initCodeHash = CodecUtil.keccak256(initCode);
        byte[] callDataHash = CodecUtil.keccak256(callData);
        byte[] paymasterAndDataHash = CodecUtil.keccak256(paymasterAndData);
        def tuples = new Tuple[senders.size()];
        for (i in 0..<senders.size()) {
            def sender = senders[i]
            def nonce = Entrypoint.getNonce(entrypoint, sender, chain)
            DynamicStruct uopStruct = new DynamicStruct(new Address(sender), new Uint256(nonce), new Bytes32(initCodeHash),
                    new Bytes32(callDataHash), new Uint256(callGasLimit), new Uint256(verificationGasLimit), new Uint256(preVerificationGas),
                    new Uint256(maxFeePerGas), new Uint256(maxPriorityFeePerGas), new Bytes32(paymasterAndDataHash))
            String uopEncode = TypeEncoder.encode(uopStruct)
            byte[] hash = CodecUtil.keccak256(uopEncode)
            String encode = CodecUtil.abiEncode(new Bytes32(hash), new Address(entrypoint), new Uint256(chain.getChainId()))
            def opHash = Numeric.toHexString(CodecUtil.keccak256(encode))
            def signature = WalletUtil.signToStringNoPrefix(WalletUtil.signPrefixedMessage(opHash, credentials.getEcKeyPair()))

            def t = Tuple.of(
                    com.esaulpaugh.headlong.abi.Address.wrap(CodecUtil.toChecksumAddress(sender)), nonce, initCode,
                    callData, callGasLimit, verificationGasLimit, preVerificationGas, maxFeePerGas, maxPriorityFeePerGas,
                    paymasterAndData, Numeric.hexStringToByteArray(signature)
            )

//            def data = Numeric.toHexString(Function.parse("simulateValidation((address,uint256,bytes,bytes,uint256,uint256,uint256,uint256,uint256,bytes,bytes))").encodeCallWithArgs(t).array())
//
//            def call = TransactionUtil.call(null, entrypoint, data, chain)
//            ChainErrorMsg chainErrorMsg = ChainErrorUtil.parseChainError(call);
//            if (FailedOp.isMatch(chainErrorMsg.getMethodId())) {
//                def failed = new FailedOp(chainErrorMsg)
//                println failed.getReason()
//                break
//            }

            tuples[i] = t

        }

        def data = Numeric.toHexString(Function.parse("handleOps((address,uint256,bytes,bytes,uint256,uint256,uint256,uint256,uint256,bytes,bytes)[],address)")
                .encodeCallWithArgs(tuples, com.esaulpaugh.headlong.abi.Address.wrap(CodecUtil.toChecksumAddress(credentials.getAddress()))).array())

        when:
        def tx = TransactionUtil.execute(chain, credentials, BigInteger.TEN.pow(6).multiply(BigInteger.valueOf(30)).divide(BigInteger.TEN), entrypoint, BigInteger.ZERO, data, false, maxFeePerGas, maxPriorityFeePerGas)
        then:
        println tx.transactionHash
    }

    def "depositTo"() {
        given:
        def chain = Chain.MATIC_MAIN
        def credentials = UserConstant.CREDENTIALS
        def entrypoint = "0x5ff137d4b0fdcd49dca30c7cf57e578a026d2789"
        def paymaster = "0xEFE9118a8A61b71B1a71e76aaf593ad755A2F3A1"
        when:
        def tx = Entrypoint.depositTo(entrypoint, paymaster, BigInteger.TEN.pow(18), credentials, chain)
        then:
        println tx.transactionHash
    }

    def "balanceOf"() {
        given:
        def chain = Chain.MATIC_MAIN
        def entrypoint = "0x5ff137d4b0fdcd49dca30c7cf57e578a026d2789"
        def paymaster = "0xEFE9118a8A61b71B1a71e76aaf593ad755A2F3A1"
        when:
        def amount = Entrypoint.balanceOf(entrypoint, paymaster, chain)
        then:
        println amount
    }
}
