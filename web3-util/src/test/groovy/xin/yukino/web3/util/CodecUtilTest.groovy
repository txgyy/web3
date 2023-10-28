package xin.yukino.web3.util

import spock.lang.Specification

class CodecUtilTest extends Specification {

    def "GetRawTransactionData"() {
        given:
        def chain = Chain.ETH_MAIN
        def txHash = "0xeddf9e61fb9d8f5111840daef55e5fde0041f5702856532cdbb5a02998033d26"
        when:
        def data = CodecUtil.getRawTransactionData(txHash, chain)
        then:
        println data
    }

    def "toChecksumAddress"(){
        given:
        def addr = "0x785a9d2b1559a9f0ec1c8d8457eb2f6d25166643";
        expect:
        println CodecUtil.toChecksumAddress(addr)
    }
}
