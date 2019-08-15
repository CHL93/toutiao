package shu.chl.toutiao.Controller;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shu.chl.toutiao.Bean.*;
import shu.chl.toutiao.Dao.CommentDao;
import shu.chl.toutiao.Dao.UserDao;
import shu.chl.toutiao.Util.ToutiaoUtil;
import shu.chl.toutiao.service.CommentService;
import shu.chl.toutiao.service.LikeService;
import shu.chl.toutiao.service.NewsService;
import shu.chl.toutiao.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/home")
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    NewsService newsService;
    @Autowired
    UserHolder userHolder;
    @Autowired
    LikeService likeService;

    @Autowired
    UserService userDao;

    @Autowired
    CommentService commentService;

    @RequestMapping(value = "/uploadImage",method = {RequestMethod.POST})
    @ResponseBody
    public String upLoadImage(@RequestParam("file") MultipartFile file){
        String imageUrl=null;
        try {
             imageUrl=newsService.saveImage(file);
            if(imageUrl==null){
                return ToutiaoUtil.getJSONString(1,"图片上传失败");
            }
        } catch (IOException e) {
            logger.error("图片上传错误"+e.getMessage());
            e.printStackTrace();
            return ToutiaoUtil.getJSONString(1,"图片上传异常");

        }
        return ToutiaoUtil.getJSONString(0,imageUrl);
    }
    @RequestMapping(value = "/image",method = {RequestMethod.GET})
    public void getImage(@RequestParam("name") String imageName, HttpServletResponse response){
        response.setContentType("image/jpeg");
        try {
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.imagePath+imageName)),response.getOutputStream());
        } catch (Exception e) {
            logger.error("读取图片异常"+e.getMessage());
            e.printStackTrace();
        }
    }
    @RequestMapping("/detail/{newsId}")
    public String detail(Model model, @PathVariable("newsId") int newsId){
        News news=newsService.selectNewsById(newsId);
       User user=userDao.selectByID(news.getUserId());
       model.addAttribute("news",news);
       model.addAttribute("owner",user);
       int commentsCount=commentService.getCommentCount(news.getId(), EntityType.ENTITY_NEWS);
       model.addAttribute("commentsCount",commentsCount);
       System.out.println(news.getId());
       List<Comment> comments=commentService.selectByEntity(news.getId(),EntityType.ENTITY_NEWS);
       List<ViewObject> commentVos=new ArrayList<>();
       for(Comment comment:comments){
           ViewObject vo=new ViewObject();
           vo.set("comment",comment);
           vo.set("user",userDao.selectByID(comment.getUserId()));
           commentVos.add(vo);
       }
       System.out.println(commentVos.size());
       model.addAttribute("commentVos",commentVos);
       return "detail";
    }
    @RequestMapping("/user/addNews/")
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link){
        News news=new News();
        User user=userHolder.getUser();
        if(user==null){
            news.setUserId(1);
        }else{
        news.setUserId(user.getId());}
        news.setTitle(title);
        news.setCreatedDate(new Date());
        news.setLink(link);
        news.setImage(image);
        try {
            newsService.addNews(news);
        } catch (Exception e) {
            logger.error("分享失败："+e.getMessage());
            e.printStackTrace();
        }
        return ToutiaoUtil.getJSONString(0,"分享资讯成功");

    }
    @RequestMapping(value = "/addComment",method ={RequestMethod.POST})
    public String addComment(Model model,@RequestParam("newsId") int newsId,@RequestParam("content") String content){
        if(content==null||content==""){
             model.addAttribute("commentError","评论不能为空");
             return "redirect:/home/detail/"+newsId;
        }
        User user=userHolder.getUser();
        Comment comment=new Comment();
        comment.setUserId(user.getId());
        comment.setEntityType(EntityType.ENTITY_NEWS);
        comment.setEntityId(newsId);
        comment.setCreatedDate(new Date());
        comment.setStatus(0);
        comment.setContent(content);
        int success=commentService.addComment(comment);
        if(success<0){
            model.addAttribute("commentError","评论添加失败");
            return "redirect:/home/detail/"+newsId;

        }
        int count=commentService.getCommentCount(newsId,EntityType.ENTITY_NEWS);
        System.out.println("newsId"+newsId);
        newsService.updateComment(newsId,count);
        return "redirect:/home/detail/"+newsId;
    }

}
