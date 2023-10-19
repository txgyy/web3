package xin.yukino.web3.util.chain.op;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.protocol.core.methods.response.EthCall;
import xin.yukino.web3.util.chain.ChainEnum;
import xin.yukino.web3.util.TransactionUtil;

import java.util.List;

public class OpL1Block {

    public static List<Type> getAllInfo(String contract, ChainEnum chain) {
        List<Type> inputParameters = Lists.newArrayList();
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Uint64.class), TypeReference.create(Uint64.class), TypeReference.create(Uint256.class), TypeReference.create(Bytes32.class), TypeReference.create(Uint64.class), TypeReference.create(Bytes32.class), TypeReference.create(Uint256.class), TypeReference.create(Uint256.class));
        Function function = new Function("getAllInfo", inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(contract, contract, data, chain);
        return FunctionReturnDecoder.decode(call.getValue(), function.getOutputParameters());
    }
}
