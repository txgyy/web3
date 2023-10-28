package xin.yukino.web3.contract.eip.eip4337.entrypoint;

import com.google.common.collect.Lists;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import xin.yukino.web3.contract.eip.eip4337.*;
import xin.yukino.web3.util.TransactionUtil;
import xin.yukino.web3.util.ChainErrorUtil;
import xin.yukino.web3.util.IChain;
import xin.yukino.web3.util.error.ChainErrorMsg;
import xin.yukino.web3.util.error.IEvmError;

import java.math.BigInteger;
import java.util.List;

public class Entrypoint {

    public EthSendTransaction handleOps(String entryPointAddress, DynamicArray<UserOperation> userOps, Credentials bounder, IChain chain) {
        Function function = new Function("handleOps", Lists.newArrayList(userOps, new Address(bounder.getAddress())), Lists.newArrayList());
        return TransactionUtil.execute(entryPointAddress, FunctionEncoder.encode(function), bounder, chain);
    }

    public EthSendTransaction handleAggregatedOps(String entryPointAddress, DynamicArray<UserOpsPerAggregator> opsPerAggregator, Credentials bounder, IChain chain) {
        Function function = new Function("handleAggregatedOps", Lists.newArrayList(opsPerAggregator, new Address(bounder.getAddress())), Lists.newArrayList());
        return TransactionUtil.execute(entryPointAddress, FunctionEncoder.encode(function), bounder, chain);
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

    public IEvmError simulateHandleOp(String entryPointAddress, UserOperation uop, IChain chain) {
        Function function = new Function("simulateHandleOp", Lists.newArrayList(uop), Lists.newArrayList());
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
        return TransactionUtil.execute(entryPointAddress, value, FunctionEncoder.encode(function), false, sender, chain);
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

    public static IEvmError parseCommonError(ChainErrorMsg chainErrorMsg) {
        if (chainErrorMsg.isMethodId(FailedOp.ERROR_METHOD_ID)) {
            return new FailedOp(chainErrorMsg);
        } else {
            return IEvmError.parseDefaultError(chainErrorMsg);
        }
    }
}
