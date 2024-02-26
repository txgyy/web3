package xin.yukino.web3.app.evm.eip.eip4337.entrypoint;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint192;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;
import xin.yukino.web3.app.evm.eip.eip4337.*;
import xin.yukino.web3.util.evm.ChainErrorUtil;
import xin.yukino.web3.util.evm.IChain;
import xin.yukino.web3.util.evm.TransactionUtil;
import xin.yukino.web3.util.evm.error.ChainErrorMsg;
import xin.yukino.web3.util.evm.error.IEvmError;

import java.math.BigInteger;
import java.util.List;

public class Entrypoint {

    public static EthSendTransaction handleOps(String entryPointAddress, UserOperation userOp, Credentials bounder, IChain chain) {
        Function function = new Function("handleOps", Lists.newArrayList(new DynamicArray<>(UserOperation.class, userOp), new Address(bounder.getAddress())), Lists.newArrayList());
        return TransactionUtil.execute(chain, bounder, BigInteger.valueOf(1000000), entryPointAddress, BigInteger.ZERO, FunctionEncoder.encode(function), false, userOp.getMaxFeePerGas().getValue(), userOp.getMaxPriorityFeePerGas().getValue());
    }

    public EthSendTransaction handleAggregatedOps(String entryPointAddress, DynamicArray<UserOpsPerAggregator> opsPerAggregator, Credentials bundler, IChain chain) {
        Function function = new Function("handleAggregatedOps", Lists.newArrayList(opsPerAggregator, new Address(bundler.getAddress())), Lists.newArrayList());
        return TransactionUtil.execute(chain, bundler, entryPointAddress, FunctionEncoder.encode(function));
    }

    public IEvmError simulateValidation(String entryPointAddress, UserOperation uop, IChain chain) {
        Function function = new Function("simulateValidation", Lists.newArrayList(uop), Lists.newArrayList());
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(entryPointAddress, entryPointAddress, data, chain);
        ChainErrorMsg chainErrorMsg = ChainErrorUtil.parseChainError(call);
        if (chainErrorMsg.isMethodId(ValidationResult.ERROR_METHOD_ID)) {
            return new ValidationResult(chainErrorMsg);

        } else if (chainErrorMsg.isMethodId(ValidationResultWithAggregation.ERROR_METHOD_ID)) {
            return new ValidationResultWithAggregation(chainErrorMsg);

        } else {
            return parseCommonError(chainErrorMsg);
        }
    }

    public static IEvmError simulateHandleOp(String entryPointAddress, UserOperation uop, IChain chain) {
        Function function = new Function("simulateHandleOp", Lists.newArrayList(uop, Address.DEFAULT, DynamicBytes.DEFAULT), Lists.newArrayList());
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(entryPointAddress, entryPointAddress, data, chain);
        ChainErrorMsg chainErrorMsg = ChainErrorUtil.parseChainError(call);
        if (chainErrorMsg.isMethodId(ExecutionResult.ERROR_METHOD_ID)) {
            return new ExecutionResult(chainErrorMsg);
        } else {
            return parseCommonError(chainErrorMsg);
        }

    }

    public IEvmError getSenderAddress(String entryPointAddress, DynamicBytes initCode, IChain chain) {
        Function function = new Function("getSenderAddress", Lists.newArrayList(initCode), Lists.newArrayList());
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(entryPointAddress, entryPointAddress, data, chain);
        ChainErrorMsg chainErrorMsg = ChainErrorUtil.parseChainError(call);
        if (chainErrorMsg.isMethodId(SenderAddressResult.ERROR_METHOD_ID)) {
            return new SenderAddressResult(chainErrorMsg);
        } else {
            return IEvmError.parseDefaultError(chainErrorMsg);
        }
    }

    public static EthSendTransaction depositTo(String entryPointAddress, String address, BigInteger value, Credentials sender, IChain chain) {
        Function function = new Function("depositTo", Lists.newArrayList(new Address(address)), Lists.newArrayList());
        return TransactionUtil.execute(chain, sender, entryPointAddress, value, FunctionEncoder.encode(function), false);
    }

    public static BigInteger balanceOf(String entryPointAddress, String accountAddress, IChain chain) {
        List<Type> inputParameters = Lists.newArrayList(new Address(accountAddress));
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Uint256.class));
        Function function = new Function("balanceOf", inputParameters, outputParameters);
        EthCall call = TransactionUtil.call(null, entryPointAddress, FunctionEncoder.encode(function), chain);
        return (BigInteger) FunctionReturnDecoder.decode(call.getValue(), function.getOutputParameters()).get(0).getValue();
    }

    public UserOpsPerAggregator aggregateSignatures(DynamicArray<UserOperation> userOps, Address aggregator, IChain chain) {
        Function function = new Function("aggregateSignatures", Lists.newArrayList(userOps), Lists.newArrayList(TypeReference.create(DynamicBytes.class)));
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(aggregator.getValue(), aggregator.getValue(), data, chain);
        List<Type> signature = FunctionReturnDecoder.decode(call.getResult(), function.getOutputParameters());
        return new UserOpsPerAggregator(userOps, aggregator, (DynamicBytes) signature.get(0));
    }

    public static BigInteger getNonce(String entryPoint, String sender, IChain chain) {
        Function function = new Function("getNonce", Lists.newArrayList(new Address(sender), new Uint192(BigInteger.ZERO)), Lists.newArrayList(TypeReference.create(Uint256.class)));
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(null, entryPoint, data, chain);
        ChainErrorUtil.throwChainError(call);
        List<Type> results = FunctionReturnDecoder.decode(call.getResult(), function.getOutputParameters());
        return ((Uint256) results.get(0)).getValue();
    }

    public static String getUserOpHash(String entryPoint, UserOperation uop, IChain chain) {
        Function function = new Function("getUserOpHash", Lists.newArrayList(uop), Lists.newArrayList(TypeReference.create(Bytes32.class)));
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(null, entryPoint, data, chain);
        ChainErrorUtil.throwChainError(call);
        List<Type> results = FunctionReturnDecoder.decode(call.getResult(), function.getOutputParameters());
        return Numeric.toHexString(((Bytes32) results.get(0)).getValue());
    }

    public static IEvmError parseCommonError(ChainErrorMsg chainErrorMsg) {
        if (FailedOp.isMatch(chainErrorMsg.getMethodId())) {
            return new FailedOp(chainErrorMsg);
        } else {
            return IEvmError.parseDefaultError(chainErrorMsg);
        }
    }
}
