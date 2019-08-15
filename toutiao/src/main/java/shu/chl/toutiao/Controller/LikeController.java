package shu.chl.toutiao.Controller;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import shu.chl.toutiao.Bean.EntityType;
import shu.chl.toutiao.Bean.UserHolder;
import shu.chl.toutiao.Util.RedisAdapter;
import shu.chl.toutiao.Util.RedisKeyUtil;
import shu.chl.toutiao.Util.ToutiaoUtil;
import shu.chl.toutiao.async.EventModel;
import shu.chl.toutiao.async.EventProducer;
import shu.chl.toutiao.async.EventType;
import shu.chl.toutiao.service.LikeService;
import shu.chl.toutiao.service.NewsService;

@Controller
public class LikeController {
    @Autowired
    LikeService likeService;
    @Autowired
    NewsService newsService;
    @Autowired
    UserHolder userHolder;

    @Autowired
    RedisAdapter redisAdapter;
    @Autowired
    EventProducer eventProducer;
    @RequestMapping("/like/{newsId}")
    public String like(@PathVariable("newsId") int newsId){
        int localId=userHolder.getUser()!=null?userHolder.getUser().getId():0;
        //点击喜欢，则添加用户到队列中，并返回喜欢人数
        long likeCount=likeService.likeNews(newsId, EntityType.ENTITY_NEWS,localId);
        //更新
        System.out.println("执行了like");
        newsService.updteLikeCount(newsId,likeCount);
        EventModel eventModel=new EventModel(EventType.LIKE);
        eventModel.setActorId(localId);
        eventModel.setEntityId(newsId);
        eventModel.setEntityOwnerId(newsService.selectNewsById(newsId).getUserId());
        eventModel.setEntityType(EntityType.ENTITY_NEWS);
        eventProducer.fireEvent(eventModel);
       // redisAdapter.lpush(RedisKeyUtil.getEventQueueKey(), JSON.toJSONString(eventModel));

        return "redirect:/";
        //return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }
    @RequestMapping("/dislike/{newsId}")
    public String disLike(@PathVariable("newsId") int newsId){
        int localId=userHolder.getUser()!=null?userHolder.getUser().getId():0;
        //点击喜欢，则添加用户到队列中，并返回喜欢人数
        long likeCount=likeService.disLikeNews(newsId, EntityType.ENTITY_NEWS,localId);
        //更新
        newsService.updteLikeCount(newsId,likeCount);
       // return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
        return "redirect:/";
    }
}
