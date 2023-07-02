package cn.ihoway.web.controller.my;

import cn.ihoway.util.AccessRoute;
import cn.ihoway.util.HowayResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * todo 我的文章、我的评论、我的消息、我的粉丝、我的关注、我的好友
 */
@RestController
@RequestMapping(value = "/my")
public class MyController {

    /**
     * 获取我的全部文章
     */
    @CrossOrigin
    @RequestMapping(value = "/essay", method = { RequestMethod.GET })
    public String getAllEssay(String token,Integer type, Integer index, Integer size, String eventNo, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        map.put("index",index);
        map.put("size",size);
        map.put("token",token);
        map.put("eventNo",eventNo);
        HowayResult rs = AccessRoute.handle(request,"myEssay",map);
        return rs.toString();
    }


    @CrossOrigin
    @RequestMapping(value = "/chat", method = { RequestMethod.GET })
    public String getChat(String token,Integer target, String eventNo, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("targetId",target);
        map.put("token",token);
        map.put("eventNo",eventNo);
        HowayResult rs = AccessRoute.handle(request,"myChat",map);
        return rs.toString();
    }

}
