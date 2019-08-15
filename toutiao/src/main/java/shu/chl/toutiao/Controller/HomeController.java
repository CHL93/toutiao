package shu.chl.toutiao.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import shu.chl.toutiao.Bean.EntityType;
import shu.chl.toutiao.Bean.News;
import shu.chl.toutiao.Bean.UserHolder;
import shu.chl.toutiao.Bean.ViewObject;
import shu.chl.toutiao.service.LikeService;
import shu.chl.toutiao.service.NewsService;
import shu.chl.toutiao.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;

    @Autowired
    UserHolder userHolder;

    @Autowired
    LikeService likeService;
    public List<ViewObject> getNews(int userId,int offset,int limit){
        int localId=userHolder.getUser()!=null?userHolder.getUser().getId():0;
        List<News> newsList= newsService.selectByUserIdAndOffset(userId,offset,limit);
        List<ViewObject> vos=new ArrayList<>();
        for(News news:newsList){
           ViewObject vo=new ViewObject();
            if (localId != 0) {
                vo.set("like", likeService.getLikeStatus(localId, EntityType.ENTITY_NEWS, news.getId()));
            } else {
                vo.set("like", 0);
            }
           vo.set("news",news);
           vo.set("user",userService.selectByID(news.getUserId()));
           vos.add(vo);
        }
        return vos;
    }
    @RequestMapping(path={"/","/index"},method ={RequestMethod.GET,RequestMethod.POST})
    public String index(Model model){
        List<ViewObject> vos=getNews(0,0,10);
        model.addAttribute("vos",vos);
        return "home";
    }
    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getNews(userId, 0, 10));
        return "home";
    }
}
