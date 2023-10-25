package xin.yukino.web3.util.web3j.req;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TracerTypeEnum {

    CALL_TRACER("callTracer"),

    PRESTATE_TRACER("prestateTracer"),

    ;

    private final String type;

}
