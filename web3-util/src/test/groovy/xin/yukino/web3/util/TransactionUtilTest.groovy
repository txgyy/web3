package xin.yukino.web3.util


import com.esaulpaugh.headlong.abi.Function
import spock.lang.Specification
import xin.yukino.web3.util.constant.Chain
import xin.yukino.web3.util.constant.UserConstant

class TransactionUtilTest extends Specification {
    def "EstimateGas"() {
        given:
        def from = "0x5ff137d4b0fdcd49dca30c7cf57e578a026d2789"
        def to = "0xe3b35a758011678bd63f1a5f6cfee807ee68f724"
        def func = Function.parse("transfer(address,uint256)")
//        def data = Numeric.toHexString(func.encodeCallWithArgs(Address.wrap(CodecUtil.toChecksumAddress(from)), BigInteger.ONE).array())
        def data = "0x70641a2200000000000000000000000024e24277e2ff8828d5d2e278764ca258c22bd497000000000000000000000000000000000000000000000000c249fdd32778000000000000000000000000000000000000000000000000000000000000000000600000000000000000000000000000000000000000000000000000000000000284d9b3d6d00000000000000000000000000000000000000000000000000000000000000040000000000000000000000000e3b35a758011678bd63f1a5f6cfee807ee68f72400000000000000000000000032d89cf0e5d87e0f287df8f511f14f4151035a3100000000000000000000000024e24277e2ff8828d5d2e278764ca258c22bd49790315a52a1d6432f853d4ec2f850fbaa3657ad839c58f6804b9ce060557f387400000000000000000000000000000000000000000000000000000000000001e000000000000000000000000000000000000000000000000000000000000001f4000000000000000000000000000000000000000000000000006379da05b600000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000006579d1ba0000000000000000000000000000000000000000000000000000000065a15eba00000000000000000000000000000000000000000000000000000000000000c800000000000000000000000000000000000000000000000000000000115181fe0000000000000000000000000000000000000000000000000000000000000220000000000000000000000000000000000000000000000000000000000000001b6152872147ca9d3c0586c763f730ddd97046212e440020bbab9d31926b5d0bea475ed2303e8adf0bd6c69f3cd6b808e81a830bc3c6c9dc3551a302a2d8d2490c00000000000000000000000000000000000000000000000000000000000000046173637400000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        def chain = Chain.AVAX_MAIN
        when:
        def gas = TransactionUtil.estimateGas(from, to, data, chain)
        then:
        println gas
    }

    def "execute"() {
        given:
        def from = UserConstant.DEX
        def to = from.address
        when:
        def transaction = TransactionUtil.execute(Chain.AVAX_MAIN, from, BigInteger.valueOf(30000), to, BigInteger.ZERO, "", false, new BigDecimal("16.77").multiply(BigDecimal.TEN.pow(9)).toBigInteger())
        then:
        println transaction
    }

    def "transfer"() {
        given:
        def from = UserConstant.CREDENTIALS
        def to = from.address
        when:
        def transaction = TransactionUtil.transfer(Chain.AVAX_MAIN, from, to, BigDecimal.ZERO)
        then:
        println transaction.transactionHash

    }
}
