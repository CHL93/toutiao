package shu.chl.toutiao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shu.chl.toutiao.Bean.News;
import shu.chl.toutiao.Bean.UserHolder;
import shu.chl.toutiao.Dao.NewsDao;
import shu.chl.toutiao.Util.ToutiaoUtil;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
      @Autowired
      NewsDao newsDao;

      @Autowired
      UserHolder userHolder;
    public int addNews(News news) throws Exception{
        int success=newsDao.addNews(news);
        if(success<1)
            throw  new Exception("添加资讯失败");
        return success;
    }
    public List<News> selectByUserIdAndOffset(int userId,int offset,int limit){
        return  newsDao.selectByUserIdAndOffset(userId,offset,limit);

    }

    public String saveImage(MultipartFile file) throws IOException {
       String imageName=file.getOriginalFilename();
       boolean  isImage= ToutiaoUtil.isImage(imageName);
       if(!isImage)
           return null;
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        String fileName= UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
        Files.copy(file.getInputStream(),new File(ToutiaoUtil.imagePath+fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
       return ToutiaoUtil.TOUTIAO_DOMAIN+"/image?name="+fileName;
    }

    public News selectNewsById(int newsId){
        return newsDao.selectByNewsId(newsId);
    }
    public int updateComment(int newsId,int count){
        return newsDao.updateComment(newsId,count);
    }
    public int updteLikeCount(int newsId,long   likeCount){
        return  newsDao.updateLikeCount(newsId,likeCount);
    }
}
