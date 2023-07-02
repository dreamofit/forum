package cn.ihoway.common;

import cn.ihoway.annotation.InputCheck;
import cn.ihoway.annotation.InsideCheck;
import cn.ihoway.api.record.RecordAsm;
import cn.ihoway.api.security.TokenAsm;
import cn.ihoway.annotation.Processor;
import cn.ihoway.api.user.UserAsm;
import cn.ihoway.common.io.CommonInput;
import cn.ihoway.common.io.CommonOutput;
import cn.ihoway.util.HowayContainer;
import cn.ihoway.type.AuthorityLevel;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayConfigReader;
import cn.ihoway.util.HowayEncrypt;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.MDC;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * 程序处理器公共类，所有逻辑处理器必须继承该类
 * @param <I>
 * @param <O>
 */
public abstract class CommonProcessor<I extends CommonInput,O extends CommonOutput> {

    private final HowayLog logger = new HowayLog(CommonProcessor.class);

    /**
     * 数据检查
     * @param input input
     * @return HowayResult
     */
    protected StatusCode dataCheck(I input){
        return StatusCode.SUCCESS;
    }

    /**
     * 安全认证
     * @param input input
     * @return HowayResult
     */
    protected HowayResult certification(I input,O output, AuthorityLevel limitAuthority) {
        HashMap<String, Object> res = getIsTokenRule(input, limitAuthority);
        if((Integer)res.get("code") == 200){
            return HowayResult.createSuccessResult(output);
        }else {
            logger.error("token:" + input.token + "失败：" + res.get("msg"));
            return HowayResult.createFailResult(StatusCode.TOKENERROR,res.get("msg").toString(),output);
        }

    }

    protected HashMap<String, Object> getIsTokenRule(I input, AuthorityLevel limitAuthority) {
        TokenAsm tokenAsm = (TokenAsm) HowayContainer.getContext().getBean("TokenAsm");
        HashMap<String,Object> res = tokenAsm.isTokenRule(input.token, limitAuthority.getLevel());
        return res;
    }

    /**
     * 程序执行之前
     * @param input
     * @param output
     * @return
     */
    protected HowayResult beforeProcess(I input, O output){
        return HowayResult.createSuccessResult(output);
    }

    /**
     * 程序主要执行部分
     * @param input input
     * @param output output
     * @return HowayResult
     */
    protected abstract HowayResult process(I input,O output);

    /**
     * 程序执行之后
     * @param input input
     * @param output output
     * @return HowayResult
     */
    protected HowayResult afterProcess(I input, O output){
        TokenAsm tokenAsm = (TokenAsm) HowayContainer.getContext().getBean("TokenAsm");
        output.token = tokenAsm.getToken(input.token, HowayConfigReader.getConfig("howay.properties","app.key"),HowayConfigReader.getConfig("howay.properties","app.secret"));
        logger.info("token:"+output.token);
        return HowayResult.createSuccessResult(output);
    }

    public HowayResult doExecute(I input, O output){
        if(StringUtils.isNotBlank(input.traceId)){
            MDC.put("traceId",input.traceId);
        }else{
            input.traceId = (String) MDC.get("traceId");
        }
        if(StringUtils.isBlank(input.eventNo)){
            return HowayResult.createFailResult(StatusCode.FIELDMISSING,"事件编号不能为空!",output);
        }
        HashMap<String,String> addInput = new HashMap<>();
        HowayResult response = insertRecord(input,output,addInput); //记录input日志
        if(response.getStatusCode() == StatusCode.DUPLICATREQUEST){
            return response;
        }
        response = inputCheck(input,output);
        if(!response.isOk()){
            return response;
        }
        response = getResponse(input,output);
        HowayResult newRes = response;
        Thread thread = new Thread(() -> {
            updateRecord(input,newRes,addInput); //记录output日志
        });
        thread.start();
        return response;
    }

    private HowayResult getResponse(I input,O output){
        try{
            Processor annotation = this.getClass().getAnnotation(Processor.class);
            logger.info("begin --> input: "+ JSON.toJSONString(input));
            StatusCode sc = dataCheck(input);
            if( sc.getCode() != StatusCode.SUCCESS.getCode() ){
                return HowayResult.createFailResult(sc,"数据检查失败",output);
            }
            if(annotation.certification()){
                HowayResult cerRes = certification(input,output,annotation.limitAuthority());
                if(!cerRes.isOk()){
                    return cerRes;
                }
            }
            HowayResult response = beforeProcess(input,output);
            if(!response.isOk()){
                return response;
            }
            response = process(input,output);
            if(!response.isOk()){
                return response;
            }
            response = afterProcess(input,output);
            if(!response.isOk()){
                return response;
            }

            logger.info("end --> response: "+ JSON.toJSONString(response));
            return response;
        }catch (Exception e){
            logger.error("[error] processor has an exception :"+Arrays.toString(e.getStackTrace()));
            return HowayResult.createFailResult(StatusCode.JAVAEXCEPTION,output);
        }
    }

    /**
     * 写日志 ———— input情况
     * @param input
     * @param output
     * @return
     */
    private HowayResult insertRecord(I input,O output,HashMap<String,String> addInput){
        try{
            RecordAsm recordAsm = (RecordAsm) HowayContainer.getContext().getBean("RecordAsm");
            HashMap<String,Object> res = recordAsm.findByEventNo(input.eventNo);
            if(res != null){
                return HowayResult.createFailResult(StatusCode.DUPLICATREQUEST,"请求重复!",output);
            }
            addInput.put("eventNo",input.eventNo);
            addInput.put("input",JSON.toJSONString(input));
            addInput.put("inputToken",input.token);
            long timeStamp = System.currentTimeMillis();
            Date date = new Date(timeStamp);
            addInput.put("inputTime",getCurrentTime(date));
            addInput.put("inputTimestamp",String.valueOf(timeStamp));
            addInput.put("sysName","forum");
            addInput.put("ip",input.ip);
            addInput.put("method",input.method.toString());
            addInput.put("traceId", input.traceId);

        }catch (Exception e){
            logger.error("[warning] input日志写入失败，cause by :" + e.getCause());
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        return HowayResult.createSuccessResult(output);
    }

    private void updateRecord(I input,HowayResult response,HashMap<String,String> addInput){
        try {
            RecordAsm recordAsm = (RecordAsm) HowayContainer.getContext().getBean("RecordAsm");
            addInput.put("eventNo",input.eventNo);
            addInput.put("output",JSON.toJSONString(response));
            long timeStamp = System.currentTimeMillis();
            Date date = new Date(timeStamp);
            addInput.put("outputTime",getCurrentTime(date));
            addInput.put("outputTimestamp",String.valueOf(timeStamp));
            addInput.put("responseCode",String.valueOf(response.getStatusCode().getCode()));
            recordAsm.addRecord(addInput);
        }catch (Exception e){
            logger.error("[warning] output日志写入失败，cause by :" + e.getCause());
            logger.error(Arrays.toString(e.getStackTrace()));
        }

    }

    protected String getCurrentTime(Date date){
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    protected HashMap<String, Object> getUserByToken(String token){
        TokenAsm tokenAsm = (TokenAsm) HowayContainer.getContext().getBean("TokenAsm");
        return tokenAsm.getUserByToken(token);
    }

    protected HashMap<String, Object> getUserById(Integer id,I input){
        UserAsm userAsm = (UserAsm) HowayContainer.getContext().getBean("UserAsm");
        return userAsm.getUserById(id,input.eventNo,input.traceId);
    }

    /**
     * 获取事件编号
     * @return eventNo
     */
    protected String getEventNo(){
        return HowayEncrypt.encrypt(UUID.randomUUID().toString(),"MD5",12);
    }

    /**
     * 检查输入类
     * @param o obj
     * @param output output
     * @return HowayResult
     */
    private HowayResult inputCheck(Object o,O output){
        Class<?> clz = o.getClass();
        InputCheck inputCheck = (InputCheck) clz.getAnnotation(InputCheck.class);
        if(inputCheck != null && inputCheck.check()){
            //获取类中的属性
            Field[] fileds = clz.getFields();
            for (Field field : fileds){
                try {
                    //获取该属性的值
                    Object obj = field.get(o);
                    //获取属性中的注解
                    InsideCheck insideCheck = field.getAnnotation(InsideCheck.class);
                    if(insideCheck != null){
                        //必输注解
                        if(insideCheck.mustInput() && (obj == null || StringUtils.isBlank(obj.toString()))){
                            logger.info(field.getName() + "必输！");
                            return HowayResult.createFailResult(StatusCode.FIELDMISSING,field.getName() + "必输！",output);
                        }
                        //长度限制注解
                        if(insideCheck.maxLen() > 0 && obj != null && obj.toString().length() > insideCheck.maxLen()){
                            logger.info(field.getName() + "长度超出限制" + insideCheck.maxLen() + " ! len : " + obj.toString().length());
                            return HowayResult.createFailResult(StatusCode.ILLEGALPARAMETER,field.getName() + "长度超出限制!" + insideCheck.maxLen(), output);
                        }
                        //最小值
                        if(obj != null && insideCheck.minValue() != Integer.MAX_VALUE && Integer.parseInt(obj.toString()) < insideCheck.minValue()){
                            logger.info(field.getName() + "最小值为" + insideCheck.minValue());
                            return HowayResult.createFailResult(StatusCode.ILLEGALPARAMETER,field.getName() + "最小值为" + insideCheck.minValue(), output);
                        }
                        //最大值
                        if(obj != null && insideCheck.maxValue() != Integer.MIN_VALUE && Integer.parseInt(obj.toString()) > insideCheck.maxValue()){
                            logger.info(field.getName() + "最大值为" + insideCheck.maxValue());
                            return HowayResult.createFailResult(StatusCode.ILLEGALPARAMETER,field.getName() + "最大值为" + insideCheck.maxValue(), output);
                        }
                        //取值范围
                        if(obj != null && !"".equals(insideCheck.ranges())){
                            String[] ranges = insideCheck.ranges().split(",");
                            boolean inRange = false;
                            for(String range : ranges){
                                if(range.equals(obj.toString())){
                                    inRange = true;
                                    break;
                                }
                            }
                            if(!inRange){
                                logger.info(field.getName() + "不在取值范围内：[" + insideCheck.ranges() + "]");
                                return HowayResult.createFailResult(StatusCode.ILLEGALPARAMETER,field.getName() + "不在取值范围内：[" + insideCheck.ranges() + "]", output);
                            }
                        }
                        //todo 格式限制注解
                        //todo 其他注解
                    }
                    if(obj == null){
                        continue;
                    }
                    HowayResult res = inputCheck(obj,output);
                    if(!res.isOk()){
                        return res;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return HowayResult.createSuccessResult(output);

        //检查input类字段
        //if(inputCheck.check()){

       // }

        //Field[] fileds = input.getClass().getFields();
        //InsideCheck annotation = this.getClass().getFields().getAnnotation(InsideCheck.class);
        //检查input内部类字段
    }

}
