package cn.ihoway.controller.operate;

import cn.ihoway.operate.OperateActionProcessor;
import cn.ihoway.operate.io.OperateActionInput;
import cn.ihoway.operate.io.OperateActionOutput;
import cn.ihoway.type.OperateAct;
import cn.ihoway.util.AccessRoute;
import cn.ihoway.util.HowayRequest;
import cn.ihoway.util.HowayResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/operate")
public class OperateController {

    @CrossOrigin
    @RequestMapping(value = "", method = { RequestMethod.POST })
    public String add(@RequestBody JSONObject operate, HttpServletRequest request){
        operate.put("action",OperateAct.toEnum(operate.getInteger("action")));
        HowayResult rs = AccessRoute.handle(request,"action",operate);
        return rs.toString();
    }
}
