package xin.yukino.web3.contract.eip.eip4337.account

import spock.lang.Specification
import xin.yukino.web3.contract.config.Chain

class AccountTest extends Specification {
    def "MasterCopy"() {

        given:
        def addr = "0xfef1aa3f31392e3ed6ed194de71f729b2eb4a43b";
        def chain = Chain.OKC_MAIN
        expect:
        println Account.masterCopy(addr, chain)
    }

    def "entryPoint"() {
        given:
        def addr = "0x1627995e3d27c937c6e8520845d3ba43a2c553d3";
        def chain = Chain.MATIC_MAIN
        expect:
        println Account.entryPoint(addr, chain)
    }

    def "getOwner"() {
        given:
        def addr = "0x1627995e3d27c937c6e8520845d3ba43a2c553d3";
        def chain = Chain.MATIC_MAIN
        expect:
        println Account.getOwner(addr, chain)
    }


}
