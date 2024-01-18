package xin.yukino.web3.util.evm;

import com.aayushatharva.brotli4j.Brotli4jLoader;
import com.aayushatharva.brotli4j.decoder.Decoder;
import com.aayushatharva.brotli4j.decoder.DirectDecompress;
import com.aayushatharva.brotli4j.encoder.Encoder;
import com.esaulpaugh.headlong.abi.Address;
import com.esaulpaugh.headlong.abi.Function;
import com.esaulpaugh.headlong.abi.Tuple;
import lombok.SneakyThrows;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.*;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.utils.Numeric;
import org.web3j.utils.Strings;
import xin.yukino.web3.util.evm.constant.Web3Constant;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class CodecUtil {

    static {
        Brotli4jLoader.ensureAvailability();
    }

    public static String decodeHexToUtfString(String hex) {
        return new String(Numeric.hexStringToByteArray(hex));
    }

    public static String buildMethodSignature(String methodName, List<Type> parameters) {
        final StringBuilder result = new StringBuilder();
        result.append(methodName);
        result.append("(");
        final String params = parameters.stream().map(Type::getTypeAsString).collect(Collectors.joining(","));
        result.append(params);
        result.append(")");
        return result.toString();
    }

    public static String buildMethodId(String methodSignature) {
        final byte[] input = methodSignature.getBytes();
        final byte[] hash = keccak256(input);
        return Numeric.toHexString(hash).substring(0, 10);
    }

    public static String abiEncodePacked(Type... parameters) {
        StringBuilder sb = new StringBuilder();
        for (Type parameter : parameters) {
            if (parameter instanceof DynamicBytes) {
                DynamicBytes parameter1 = (DynamicBytes) parameter;
                sb.append(Numeric.toHexStringNoPrefix(parameter1.getValue()));
            } else {
                sb.append(TypeEncoder.encodePacked(parameter));
            }
        }
        return sb.toString();
    }

    public static String abiEncode(Type... parameters) {
        return TypeEncoder.encode(new DynamicStruct(parameters));
    }


    public static List<Type> decodeError(String dataWithMethodSig, Event event) {
        String error = dataWithMethodSig.substring(Web3Constant.METHOD_ID_LENGTH);
        return FunctionReturnDecoder.decode(error, event.getParameters());
    }

    public static Tuple decodeFunctionOrError(String input, String methodSig) {
        Function func = Function.parse(methodSig.replace(" ", ""));
        return func.decodeCall(Numeric.hexStringToByteArray(input));
    }

    public static Tuple decodeStruct(String input, String struct) {
        Function func = Function.parse("()", struct.replace(" ", ""));
        return func.decodeReturn(Numeric.hexStringToByteArray(input));
    }


    public static String cleanHexPrefix(String input) {
        return Numeric.cleanHexPrefix(input);
    }

    public static byte[] keccak256(byte[] input) {
        return keccak256(input, 0, input.length);
    }

    public static byte[] keccak256(byte[] input, int offset, int length) {
        return Hash.sha3(input, offset, length);
    }

    public static byte[] keccak256(String input) {
        return Hash.sha3(Numeric.hexStringToByteArray(input));
    }

    public static boolean hasValidMethodId(String value) {
        return value.length() >= Web3Constant.METHOD_ID_LENGTH;
    }

    @SneakyThrows
    public static byte[] brotliFastCompress(byte[] bytes) {
        return brotliCompress(bytes, 0, 22);
    }

    @SneakyThrows
    public static byte[] brotliCompress(byte[] bytes, int quality, int lgWin) {
        Encoder.Parameters parameters = new Encoder.Parameters();
        parameters.setQuality(quality);
        parameters.setWindow(lgWin);
        parameters.setMode(Encoder.Mode.GENERIC);
        return Encoder.compress(bytes, parameters);
    }

    @SneakyThrows
    public static byte[] brotliDecompress(byte[] bytes) {
        DirectDecompress decompress = Decoder.decompress(bytes);
        return decompress.getDecompressedData();
    }

    public static String create2(String salt, String bytecodeHash, String deployer) {
        byte[] input = Numeric.hexStringToByteArray("0xff" + Numeric.cleanHexPrefix(deployer) + Numeric.cleanHexPrefix(salt) + Numeric.cleanHexPrefix(bytecodeHash));
        byte[] addressBytes = Hash.sha3(input);
        return Numeric.toHexString(addressBytes, 12, 20, true);
    }

    public static RlpList rlpDecode(String signedData) {
        return RlpDecoder.decode(Numeric.hexStringToByteArray(signedData));
    }

    public static String getRawTransactionData(String txHash, IChain chain) {
        Transaction transaction = ReceiptUtil.getTransactionByHash(txHash, chain);
        RawTransaction rawTransaction = RawTransaction.createContractTransaction(transaction.getNonce(), transaction.getGasPrice(), transaction.getGas(), transaction.getValue(), transaction.getInput());
        Sign.SignatureData signatureData = new Sign.SignatureData(Numeric.hexStringToByteArray(Numeric.encodeQuantity(BigInteger.valueOf(transaction.getV()))), Numeric.hexStringToByteArray(transaction.getR()), Numeric.hexStringToByteArray(transaction.getS()));
        return Numeric.toHexString(TransactionEncoder.encode(rawTransaction, signatureData));
    }

    public static String toChecksumAddress(String address) {
        return Address.toChecksumAddress(address);
    }

    public static String getAddressMapSlot(String addr, BigInteger index) {
        return getAddressMapSlot(addr, Numeric.toHexStringNoPrefixZeroPadded(index, 64));
    }

    public static String getAddressMapSlot(String addr, String index) {
        return Numeric.toHexString(CodecUtil.keccak256(Strings.zeros(24) + Numeric.cleanHexPrefix(addr) + Numeric.cleanHexPrefix(index)));
    }

    public static String encodeCallData(String methodSig, Object... args) {
        Function func = Function.parse(methodSig);
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (!(arg instanceof String)) {
                continue;
            }
            String arg1 = (String) arg;
            if (arg1.length() != Web3Constant.ADDRESS_LENGTH_WITH_PREFIX) {
                continue;
            }

            arg1 = Address.toChecksumAddress(arg1);
            args[i] = Address.wrap(arg1);
        }
        return Numeric.toHexString(func.encodeCallWithArgs(args).array());
    }

    public static String generateTransactionHashHexEncoded(
            RawTransaction rawTransaction, long chainId, Credentials credentials) {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        return Numeric.toHexString(Hash.sha3(signedMessage));
    }

    public static String hexToStr(String hex) {
        return new String(Numeric.hexStringToByteArray(hex));
    }
}
