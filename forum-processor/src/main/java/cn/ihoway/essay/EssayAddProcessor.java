package cn.ihoway.essay;

import cn.ihoway.annotation.Processor;
import cn.ihoway.api.HSearchAsm;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.util.HowayContainer;
import cn.ihoway.entity.Essay;
import cn.ihoway.essay.io.EssayAddInput;
import cn.ihoway.essay.io.EssayAddOutput;
import cn.ihoway.file.WriteDb;
import cn.ihoway.impl.EssayServiceImpl;
import cn.ihoway.service.EssayService;
import cn.ihoway.type.AuthorityLevel;
import cn.ihoway.type.EssayType;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.Convert;
import cn.ihoway.util.HowayConfigReader;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发表文章处理器
 */
@Processor(name = "EssayAddProcessor",certification = true,limitAuthority = AuthorityLevel.COMMONMEMBER)
public class EssayAddProcessor extends CommonProcessor<EssayAddInput, EssayAddOutput> {

    private final HowayLog logger = new HowayLog(EssayAddProcessor.class);
    private final EssayService essayService = new EssayServiceImpl();

    @Override
    protected StatusCode dataCheck(EssayAddInput input) {
        if(StringUtils.isBlank(input.inChomm.title) || input.inChomm.type == null){
            logger.info("EssayAddProcessor dataCheck error:必输字段缺少(tile,type)");
            return StatusCode.FIELDMISSING;
        }
        if(StringUtils.isBlank(input.inChomm.content) && StringUtils.isBlank(input.inChomm.text)){
            logger.info("EssayAddProcessor dataCheck error:选输字段缺少(content,text)");
            return StatusCode.FIELDMISSING;
        }
        if(input.inChomm.type > 3 || input.inChomm.type <0){
            logger.info("EssayAddProcessor dataCheck error:type只能为[0 - 3]");
            return StatusCode.ILLEGALPARAMETER;
        }
        if((input.inChomm.readPermissions != null && input.inChomm.readPermissions < 0)
                || (input.inChomm.commentPermission != null && input.inChomm.commentPermission < 0)){
            logger.info("EssayAddProcessor dataCheck error:permissions不能小于0");
            return StatusCode.ILLEGALPARAMETER;
        }
        return StatusCode.SUCCESS;
    }

    @Override
    protected HowayResult process(EssayAddInput input, EssayAddOutput output) {
        logger.info("EssayAddProcessor process begin");
        HashMap<String, Object> user = getUserByToken(input.token);
        input.inChomm.author = (Integer) user.get("id");
        Integer role = (Integer) user.get("role");
        if(input.inChomm.type == EssayType.INFORMATION.getType() && role < AuthorityLevel.ADMINISTRATOR.getLevel()){
            logger.info("资讯类文章需要管理员权限！");
            return HowayResult.createFailResult(StatusCode.PERMISSIONDENIED,output);
        }
        if(StringUtils.isNotBlank(input.inChomm.text)){
            logger.info(input.inChomm.text);
            String name = "/"+getEventNo()+".html";
            input.inChomm.url =  HowayConfigReader.getConfig("howay.properties","essay.url") + input.inChomm.author + name;
            WriteDb.writeEssay(input.inChomm.text,String.valueOf(input.inChomm.author),name);
            input.inChomm.content = input.inChomm.text;
        }
        Essay essay = packToEssay(input);
        String time = getCurrentTime(new Date(System.currentTimeMillis()));
        essay.setCreateTime(time);
        essay.setUpdateTime(time);
        essay.setStatus(0);
        if(essayService.addEssay(essay) > 0 ){
            try {
                HSearchAsm hs = (HSearchAsm) HowayContainer.getContext().getBean("HSearchAsm");
                List<Map<String, String>> docList = new ArrayList<>();
                Map<String,String> doc = new HashMap<>();
                //todo 添加一个文档则生成一次索引
                doc.put("id",String.valueOf(essay.getId()));
                doc.put("url","/essay/" + essay.getId());
                doc.put("content",input.inChomm.content);
                doc.put("title",input.inChomm.title);
                docList.add(doc);
                hs.createIndex(docList,HowayConfigReader.getConfig("howay.properties","app.name"),input.eventNo,input.traceId);
                logger.info("文章成功添加到索引");
            }catch (Exception e){
                logger.error("添加索引失败！ error:" + Arrays.toString(e.getStackTrace()));
            }
            logger.info("写入文章成功");
            return HowayResult.createSuccessResult(output);
        }
        logger.info("插入失败");
        return HowayResult.createFailResult(StatusCode.INSERTERROR,output);
    }

    private Essay packToEssay(EssayAddInput input){
        Convert convert = new Convert();
        return (Essay) convert.inputToBean(Essay.class.getName(),EssayAddInput.class.getName()+"$InChomm",input.inChomm);
    }

}
