package xin.yukino.web3.util.btc

import spock.lang.*

class BtcReceiptUtilsTest extends Specification {

    @Unroll
    def "test get Peer Mempool Transaction"() {
        when:
        def result = BtcReceiptUtils.getPeerMempoolTransaction(txId)

        assert expectedResult == false //todo - validate something

        then:
        result == expectedResult

        where:
        txId   || expectedResult
        "txId" || true
    }
}

