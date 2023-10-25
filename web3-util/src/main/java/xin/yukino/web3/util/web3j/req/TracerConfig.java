package xin.yukino.web3.util.web3j.req;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TracerConfig {

    private boolean onlyTopCall;

}
