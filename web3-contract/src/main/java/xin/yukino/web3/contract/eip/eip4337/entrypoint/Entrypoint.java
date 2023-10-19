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
import xin.yukino.web3.util.Web3ErrorUtil;
import xin.yukino.web3.util.chain.ChainEnum;
import xin.yukino.web3.util.error.EvmErrorMsg;
import xin.yukino.web3.util.error.IEvmError;

import java.math.BigInteger;
import java.util.List;

public class Entrypoint {

    public EthSendTransaction handleOps(String entryPointAddress, DynamicArray<UserOperation> userOps, Credentials bounder, ChainEnum chain) {
        Function function = new Function("handleOps", Lists.newArrayList(userOps, new Address(bounder.getAddress())), Lists.newArrayList());
        return TransactionUtil.execute(entryPointAddress, FunctionEncoder.encode(function), bounder, chain);
    }

    public EthSendTransaction handleAggregatedOps(String entryPointAddress, DynamicArray<UserOpsPerAggregator> opsPerAggregator, Credentials bounder, ChainEnum chain) {
        Function function = new Function("handleAggregatedOps", Lists.newArrayList(opsPerAggregator, new Address(bounder.getAddress())), Lists.newArrayList());
        return TransactionUtil.execute(entryPointAddress, FunctionEncoder.encode(function), bounder, chain);
    }

    public IEvmError simulateValidation(String entryPointAddress, UserOperation uop, ChainEnum chain) {
        Function function = new Function("simulateValidation", Lists.newArrayList(uop), Lists.newArrayList());
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(entryPointAddress, entryPointAddress, data, chain);
        EvmErrorMsg evmErrorMsg = Web3ErrorUtil.parseEvmError(call);
        if (evmErrorMsg.isMethodId(ValidationResult.ERROR_METHOD_ID)) {
            return new ValidationResult(evmErrorMsg);

        } else if (evmErrorMsg.isMethodId(ValidationResultWithAggregation.ERROR_METHOD_ID)) {
            return new ValidationResultWithAggregation(evmErrorMsg);

        } else {
            return parseCommonError(evmErrorMsg);
        }
    }

    public IEvmError simulateHandleOp(String entryPointAddress, UserOperation uop, ChainEnum chain) {
        Function function = new Function("simulateHandleOp", Lists.newArrayList(uop), Lists.newArrayList());
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(entryPointAddress, entryPointAddress, data, chain);
        EvmErrorMsg evmErrorMsg = Web3ErrorUtil.parseEvmError(call);
        if (evmErrorMsg.isMethodId(ExecutionResult.ERROR_METHOD_ID)) {
            return new ExecutionResult(evmErrorMsg);
        } else {
            return parseCommonError(evmErrorMsg);
        }

    }

    public IEvmError getSenderAddress(String entryPointAddress, DynamicBytes initCode, ChainEnum chain) {
        Function function = new Function("getSenderAddress", Lists.newArrayList(initCode), Lists.newArrayList());
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(entryPointAddress, entryPointAddress, data, chain);
        EvmErrorMsg evmErrorMsg = Web3ErrorUtil.parseEvmError(call);
        if (evmErrorMsg.isMethodId(SenderAddressResult.ERROR_METHOD_ID)) {
            return new SenderAddressResult(evmErrorMsg);
        } else {
            return IEvmError.parseDefaultError(evmErrorMsg);
        }
    }

    public static EthSendTransaction depositTo(String entryPointAddress, String address, BigInteger value, Credentials sender, ChainEnum chain) {
        Function function = new Function("depositTo", Lists.newArrayList(new Address(address)), Lists.newArrayList());
        return TransactionUtil.execute(entryPointAddress, value, FunctionEncoder.encode(function), false, sender, chain);
    }

    public static BigInteger balanceOf(String entryPointAddress, String accountAddress, ChainEnum chain) {
        List<Type> inputParameters = Lists.newArrayList(new Address(accountAddress));
        List<TypeReference<?>> outputParameters = Lists.newArrayList(TypeReference.create(Uint256.class));
        Function function = new Function("balanceOf", inputParameters, outputParameters);
        EthCall call = TransactionUtil.call(null, entryPointAddress, FunctionEncoder.encode(function), chain);
        return (BigInteger) FunctionReturnDecoder.decode(call.getValue(), function.getOutputParameters()).get(0).getValue();
    }

    public UserOpsPerAggregator aggregateSignatures(DynamicArray<UserOperation> userOps, Address aggregator, ChainEnum chain) {
        Function function = new Function("aggregateSignatures", Lists.newArrayList(userOps), Lists.newArrayList(TypeReference.create(DynamicBytes.class)));
        String data = FunctionEncoder.encode(function);
        EthCall call = TransactionUtil.call(aggregator.getValue(), aggregator.getValue(), data, chain);
        List<Type> signature = FunctionReturnDecoder.decode(call.getResult(), function.getOutputParameters());
        return new UserOpsPerAggregator(userOps, aggregator, (DynamicBytes) signature.get(0));
    }

    public static IEvmError parseCommonError(EvmErrorMsg evmErrorMsg) {
        if (evmErrorMsg.isMethodId(FailedOp.ERROR_METHOD_ID)) {
            return new FailedOp(evmErrorMsg);
        } else {
            return IEvmError.parseDefaultError(evmErrorMsg);
        }
    }
}
