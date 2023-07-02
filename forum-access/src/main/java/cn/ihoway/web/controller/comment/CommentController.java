package cn.ihoway.web.controller.comment;

import cn.ihoway.util.AccessRoute;
import cn.ihoway.util.HowayResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/comment")
public class CommentController {
    @CrossOrigin
    @RequestMapping(value = "", method = { RequestMethod.POST })
    public String add( @RequestBody JSONObject comment, HttpServletRequest request){
        HowayResult rs = AccessRoute.handle(request,"commentAdd",comment);
        return rs.toString();
    }

    @CrossOrigin
    @RequestMapping(value = "/{type}/{id}", method = { RequestMethod.GET })
    public String get(String token,@PathVariable("type") Integer type,@PathVariable("id") Integer id,
                      Integer floorIndex,Integer floorSize,Integer layerIndex,Integer layerSize,
                      String eventNo,HttpServletRequest request){

        Map<String,Object> map = new HashMap<>();
        if(type == 1){
            map.put("essayId",id);
            map.put("floorIndex",floorIndex);
            map.put("floorSize",floorSize);
        }else {
            map.put("commentId",id);
            map.put("layerIndex",layerIndex);
            map.put("layerSize",layerSize);
        }
        map.put("token",token);
        map.put("eventNo",eventNo);
        HowayResult rs = AccessRoute.handle(request,"commentsByBelongId",map);
        return rs.toString();
    }
}
