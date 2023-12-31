package xin.yukino.web3.util.web3j.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TraceConfig {

    private String tracer;

    private TracerConfig tracerConfig;

    private String timeout;
}
