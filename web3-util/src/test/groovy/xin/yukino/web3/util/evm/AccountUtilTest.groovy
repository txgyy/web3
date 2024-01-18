package xin.yukino.web3.util.evm

import spock.lang.Specification

class AccountUtilTest extends Specification {
    def "GetNonce"() {
        given:
        AccountUtil.getNonce("0x3fab184622dc19b6109349b94811493bf2a45362", true, Chain.LOCAL)
    }

    def "GetBalance"() {
    }

    def "TestGetBalance"() {
    }

    def "GetCode"() {
    }
}
