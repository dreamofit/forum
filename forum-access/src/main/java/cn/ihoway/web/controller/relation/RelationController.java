package cn.ihoway.web.controller.relation;


import cn.ihoway.type.RelationAct;
import cn.ihoway.util.AccessRoute;
import cn.ihoway.util.HowayResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/relation")
public class RelationController {

    /**
     * 新增一个关系
     */
    @CrossOrigin
    @RequestMapping(value = "", method = { RequestMethod.POST })
    public String add(String token, @RequestBody JSONObject relation, HttpServletRequest request){
        relation.put("type",1);
        relation.put("action",RelationAct.toEnum(relation.getInteger("action")));
        HowayResult rs = AccessRoute.handle(request,"relation",relation);
        return rs.toString();
    }
}
