package xin.yukino.web3.app.evm.solidity.opcode;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.EthCall;
import xin.yukino.web3.util.evm.IChain;
import xin.yukino.web3.util.evm.TransactionUtil;

import java.util.List;

public class Offset {

    public static Bytes32 hash(String contract, Struct struct, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList(struct);
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Bytes32.class));
        Function function = new Function("hash", inputParameters, outputParameters);
        String encode = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(contract, contract, encode, chain);
        return (Bytes32) FunctionReturnDecoder.decode(call.getValue(), function.getOutputParameters()).get(0);
    }

    public static Bytes32 hash(String contract, StructWithoutD struct, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList(struct);
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Bytes32.class));
        Function function = new Function("hash", inputParameters, outputParameters);
        String encode = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(contract, contract, encode, chain);
        return (Bytes32) FunctionReturnDecoder.decode(call.getValue(), function.getOutputParameters()).get(0);
    }

    @Getter
    static class Struct extends DynamicStruct {

        private final Address a;

        private final Uint256 b;

        private final DynamicBytes c;

        private final DynamicBytes d;

        public Struct(Address a, Uint256 b, DynamicBytes c, DynamicBytes d) {
            super(a, b, c, d);
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        public Struct(String a, Integer b, byte[] c, byte[] d) {
            this(new Address(a), new Uint256(b), new DynamicBytes(c), new DynamicBytes(d));
        }

    }

    @Getter
    static class StructWithoutD extends DynamicStruct {

        private final Address a;

        private final Uint256 b;

        private final DynamicBytes c;


        public StructWithoutD(Address a, Uint256 b, DynamicBytes c) {
            super(a, b, c);
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public StructWithoutD(String a, Integer b, byte[] c) {
            this(new Address(a), new Uint256(b), new DynamicBytes(c));
        }

    }
}
