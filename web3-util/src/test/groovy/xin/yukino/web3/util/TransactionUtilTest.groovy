package xin.yukino.web3.util

import com.esaulpaugh.headlong.abi.Address
import com.esaulpaugh.headlong.abi.Function
import org.web3j.utils.Numeric
import spock.lang.Specification
import xin.yukino.web3.util.constant.Chain

class TransactionUtilTest extends Specification {
    def "EstimateGas"() {
        given:
        def from = "0xdca9b7e52000e5e0ae07a23efaab8613c5f7966b"
        def to = "0x14feE680690900BA0ccCfC76AD70Fd1b95D10e16"
        def func = Function.parse("transfer(address,uint256)")
        def data = Numeric.toHexString(func.encodeCallWithArgs(Address.wrap(CodecUtil.toChecksumAddress(from)), BigInteger.ONE).array())
        when:
        def gas = TransactionUtil.estimateGas(from, to, data, Chain.ETH_MAIN)
        then:
        println gas
    }
}
