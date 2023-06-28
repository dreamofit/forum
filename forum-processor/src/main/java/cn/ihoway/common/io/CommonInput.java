package cn.ihoway.common.io;


import cn.ihoway.annotation.InsideCheck;
import cn.ihoway.common.CommonSeria;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 【强制】 所有input类必须继承CommonInput
 * 【强制】 所有input类内部类必须继承CommonSeria
 */
public class CommonInput extends CommonSeria {

    @InsideCheck(mustInput = true)
    public String token;

    @InsideCheck(mustInput = true)
    public String eventNo;

    @InsideCheck(mustInput = true)
    public String ip;

    @InsideCheck(mustInput = true)
    public String method;

    @InsideCheck(mustInput = true)
    public String traceId;
}
