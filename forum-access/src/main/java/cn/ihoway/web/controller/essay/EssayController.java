package cn.ihoway.web.controller.essay;

import cn.ihoway.type.Permission;
import cn.ihoway.util.AccessRoute;
import cn.ihoway.util.HowayResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/essay")
public class EssayController {

    @CrossOrigin
    @RequestMapping(value = "", method = { RequestMethod.POST })
    public String add(@RequestBody JSONObject essay, HttpServletRequest request){
        essay.put("commentPermission",essay.getInteger("commentPermission") == null ?
                Permission.PUBLIC.getValue() : essay.getInteger("commentPermission"));
        essay.put("readPermissions",essay.getInteger("readPermissions") == null ?
                Permission.PUBLIC.getValue() : essay.getInteger("readPermissions"));
        HowayResult rs = AccessRoute.handle(request,"essayAdd",essay);
        return rs.toString();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = { RequestMethod.GET })
    public String getAll(Integer type,Integer index,Integer size,String eventNo,String token,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        map.put("index",index);
        map.put("size",size);
        map.put("eventNo",eventNo);
        map.put("token",token);
        HowayResult rs = AccessRoute.handle(request,"essaySearch",map);
        return rs.toString();
    }

    @CrossOrigin
    @RequestMapping(value = "/{eid}", method = { RequestMethod.GET })
    public String get(String token,@PathVariable("eid") Integer eid, String eventNo,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("essayId",eid);
        map.put("token",token);
        map.put("eventNo",eventNo);
        HowayResult rs = AccessRoute.handle(request,"essayDetail",map);
        return rs.toString();
    }


}
