package xin.yukino.web3.app.evm.eip.eip4337.entrypoint

import com.esaulpaugh.headlong.abi.Function
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.DynamicBytes
import org.web3j.abi.datatypes.generated.Uint256
import spock.lang.Specification
import xin.yukino.web3.app.evm.config.Chain
import xin.yukino.web3.app.evm.constant.UserConstant
import xin.yukino.web3.app.evm.eip.eip4337.ExecutionResult
import xin.yukino.web3.app.evm.eip.eip4337.FailedOp
import xin.yukino.web3.app.evm.eip.eip4337.UserOperation
import xin.yukino.web3.util.evm.CodecUtil
import xin.yukino.web3.util.evm.TransactionUtil
import xin.yukino.web3.util.evm.WalletUtil

class EntrypointTest extends Specification {

    def "DepositTo"() {
        expect:
        def tx = Entrypoint.depositTo(
                "0x5ff137d4b0fdcd49dca30c7cf57e578a026d2789",
                "0xcf30680be271b8ae26737543d225660512f06672",
                BigInteger.TEN.pow(18).multiply(BigInteger.valueOf(2)),
                UserConstant.PRIVATE_KEY,
                Chain.MATIC_MAIN)
        println tx.transactionHash
    }


    def "simulateHandleOp"() {
        given:
        def chain = Chain.MATIC_MAIN
        Address entrypoint = new Address("0x5ff137d4b0fdcd49dca30c7cf57e578a026d2789")
        Address sender = new Address("0xaAF2CEDeAfe4267465771385286a54B9336F3d3d")
        Uint256 nonce = new Uint256(Entrypoint.getNonce(entrypoint.getValue(), sender.getValue(), chain))
        DynamicBytes initCode = DynamicBytes.DEFAULT
        DynamicBytes callData = new DynamicBytes(new Function("execute(address,uint256,bytes)").encodeCallWithArgs(com.esaulpaugh.headlong.abi.Address.wrap(CodecUtil.toChecksumAddress("0x40109567a8Ba77ab35B0781cDdD37277DE3d5Ca3")), BigInteger.ZERO, new Function("t()").encodeCallWithArgs().array()).array())
//        Uint256 callGasLimit = new Uint256(TransactionUtil.estimateGas(entrypoint.getValue(), sender.getValue(), Numeric.toHexString(callData.getValue()), chain))
        Uint256 callGasLimit = new Uint256(BigInteger.valueOf(100000))
        Uint256 verificationGasLimit = new Uint256(BigInteger.valueOf(39631))
        // 22276
        // 48640 61907 - 39631

        Uint256 preVerificationGas = new Uint256(BigInteger.valueOf(50000))
        def price = TransactionUtil.get1559GasPrice(chain, 80D)
        Uint256 maxFeePerGas = new Uint256(price.getMaxPriorityFeePerGas() + price.getBaseFee() * 2)
        Uint256 maxPriorityFeePerGas = maxFeePerGas
        DynamicBytes paymasterAndData = DynamicBytes.DEFAULT
        def uop = new UserOperation(sender, nonce, initCode, callData, callGasLimit, verificationGasLimit, preVerificationGas, maxFeePerGas, maxPriorityFeePerGas, paymasterAndData, DynamicBytes.DEFAULT)
        def opHash = Entrypoint.getUserOpHash(entrypoint.getValue(), uop, chain)
        DynamicBytes signature = new DynamicBytes(WalletUtil.signToBytes(WalletUtil.signPrefixedMessage(opHash, UserConstant.PRIVATE_KEY.getEcKeyPair())))
        uop = new UserOperation(sender, nonce, initCode, callData, callGasLimit, verificationGasLimit, preVerificationGas, maxFeePerGas, maxPriorityFeePerGas, paymasterAndData, signature)

        expect:
        def op = Entrypoint.simulateHandleOp(entrypoint.getValue(), uop, chain)
        if (op instanceof ExecutionResult) {
            verificationGasLimit = new Uint256(((ExecutionResult) op).preOpGas.getValue() - preVerificationGas.getValue());
            println(verificationGasLimit.getValue())
            callGasLimit = new Uint256(((ExecutionResult) op).paid.getValue().divide(maxFeePerGas.getValue()) - ((ExecutionResult) op).preOpGas.getValue())
            println(callGasLimit.getValue())

//            def t = Empty.withdraw("0x40109567a8Ba77ab35B0781cDdD37277DE3d5Ca3", UserConstant.PRIVATE_KEY, chain)
//            println t.transactionHash
//            TimeUnit.SECONDS.sleep(10)
            uop = new UserOperation(sender, nonce, initCode, callData, callGasLimit, verificationGasLimit, preVerificationGas, maxFeePerGas, maxPriorityFeePerGas, paymasterAndData, DynamicBytes.DEFAULT)
            opHash = Entrypoint.getUserOpHash(entrypoint.getValue(), uop, chain)
            signature = new DynamicBytes(WalletUtil.signToBytes(WalletUtil.signPrefixedMessage(opHash, UserConstant.PRIVATE_KEY.getEcKeyPair())))
            uop = new UserOperation(sender, nonce, initCode, callData, callGasLimit, verificationGasLimit, preVerificationGas, maxFeePerGas, maxPriorityFeePerGas, paymasterAndData, signature)
            def tx = Entrypoint.handleOps(entrypoint.getValue(), uop, UserConstant.PRIVATE_KEY, chain)
            println tx.getTransactionHash()
        } else if (op instanceof FailedOp) {
            println(((FailedOp) op).getReason())
        }
    }
}
