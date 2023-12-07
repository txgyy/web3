package xin.yukino.web3.contract.eip.eip20

import com.esaulpaugh.headlong.abi.Function
import org.web3j.utils.Numeric
import spock.lang.Specification
import xin.yukino.web3.contract.config.Chain
import xin.yukino.web3.util.TransactionUtil
import xin.yukino.web3.util.web3j.req.OverrideAccount
import xin.yukino.web3.util.web3j.req.StateOverride

/**
 * @author yukino.xin
 * @date 2023/11/7 10:17
 */
class Test extends Specification {

    def "TEST"() {
        given:
        def chain = Chain.MATIC_MAIN
        def from = "0x1234567890123456789012345678901234567890"
        def contract = "0x1234567890123456789012345678901234567890"
        def overrideAccount = new OverrideAccount()
        overrideAccount.setCode("0x6080604052348015600f57600080fd5b506004361060285760003560e01c80636c8ae38c14602d575b600080fd5b60336035565b005b6040517f6ca7b80600000000000000000000000000000000000000000000000000000000815230600482015260240160405180910390fdfea2646970667358221220af074c653fbdef02c6ba49bf295078655dbd00ea4b246f5a41ca319d8f7158f164736f6c63430008110033")
        def override = new StateOverride()
        override.put(contract, overrideAccount)

        def function = Function.parse("r()")
        def data = Numeric.toHexString(function.encodeCallWithArgs().array())
        when:
        def call = TransactionUtil.call(from, contract, data, chain, override)
        then:
        println call
    }

}
