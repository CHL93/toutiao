package shu.chl.toutiao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shu.chl.toutiao.Bean.Comment;
import shu.chl.toutiao.Dao.CommentDao;

import java.util.List;
@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;

    public int getComment(int entityId,int entityType){
        return  commentDao.getCommentCount(entityId,entityType);
    }
    public List<Comment> selectByEntity(int entityId,int entityType){
        return commentDao.selectByEntity(entityId,entityType);
    }
    public int addComment(Comment comment){
        if(comment.getContent()==null||comment.getContent()=="")
            return -1;
        return commentDao.addComment(comment);
    }

    public int getCommentCount(int id, int entityNews) {
        return commentDao.getCommentCount(id,entityNews);
    }
}
