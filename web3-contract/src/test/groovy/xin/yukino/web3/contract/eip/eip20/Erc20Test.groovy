package xin.yukino.web3.contract.eip.eip20

import com.esaulpaugh.headlong.abi.Address
import com.esaulpaugh.headlong.abi.Function
import org.web3j.utils.Numeric
import spock.lang.Specification
import xin.yukino.web3.contract.config.Chain
import xin.yukino.web3.util.TransactionUtil

/**
 * @author yukino.xin
 * @date 2023/11/7 10:17
 */
class Erc20Test extends Specification {
    def "Name"() {
    }

    def "Symbol"() {
    }

    def "Decimals"() {
    }

    def "BalanceOf"() {
        given:
        def chain = Chain.MATIC_MAIN
        def contract = "0x95A61d85888fbdFF5829e54F5D6b124c3208A30A"
        def from = "0x1234567890123456789012345678901234567890"
        when:
        def balance = Erc20.balanceOf(contract, from, chain)
        then:
        println balance
    }

    def "Mint"() {
        given:
        def chain = Chain.MATIC_MAIN
        def contract = "0x95A61d85888fbdFF5829e54F5D6b124c3208A30A"
        def from = "0x1234567890123456789012345678901234567890"
        when:
        def balance = Erc20.mint(contract, from, chain)
        then:
        println balance
    }

    def "TransferFrom"() {
    }

    def "Transfer"() {
        given:
        def chain = Chain.BSC_MAIN
        def from = "0x81dff00d2df6378f9583dc8749aa48ddbe1a5680"
//        def from = "0x29615f19d178f254a56f3c15fddc31985e7d0f21"
        def contract = "0xffff132b6b3010bfa7e9e16631bb3942bdd94461"
        def function = Function.parse("transfer(address,uint256)", "(bool)")
        def data = Numeric.toHexString(function.encodeCallWithArgs(Address.wrap(Address.toChecksumAddress("0x29615f19d178f254a56f3c15fddc31985e7d0f21")), BigInteger.ONE).array())
        when:
        def call = TransactionUtil.call(from, contract, data, chain)
        then:
        println call
    }

    def "Approve"() {
    }
}
