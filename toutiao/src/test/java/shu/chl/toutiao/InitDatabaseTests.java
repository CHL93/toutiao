package shu.chl.toutiao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import shu.chl.toutiao.Bean.Comment;
import shu.chl.toutiao.Bean.Message;
import shu.chl.toutiao.Bean.News;
import shu.chl.toutiao.Bean.User;
import shu.chl.toutiao.Dao.CommentDao;
import shu.chl.toutiao.Dao.MessageDao;
import shu.chl.toutiao.Dao.NewsDao;
import shu.chl.toutiao.Dao.UserDao;


import javax.activation.CommandMap;
import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest

public class InitDatabaseTests {
    @Autowired
    UserDao userDAO;
    @Autowired
    NewsDao newsDAO;
    @Autowired
    CommentDao commentDao;
    @Autowired
    MessageDao messageDao;

    @Test
    public void initData() {
//        Message message=new Message();
//        message.setContent("我很好");
//        message.setConversationId("13_15");
//        message.setCreatedDate(new Date());
//        message.setFromId(15);
//        message.setToId(13);
//        message.setHasRead(1);
//        messageDao.addMessage(message);
//        Assert.assertNotNull(messageDao.getConversationList(15,0,10));
      // message.setContent("我也是");


        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);

            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*5*i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i+1);
            news.setUserId(i+1);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            newsDAO.addNews(news);

            user.setPassword("newpassword");
            userDAO.updatePassword(user);
//
//            LoginTicket ticket = new LoginTicket();
//            ticket.setStatus(0);
//            ticket.setUserId(i+1);
//            ticket.setExpired(date);
//            ticket.setTicket(String.format("TICKET%d", i+1));
//            loginTicketDAO.addTicket(ticket);
//
//            loginTicketDAO.updateStatus(ticket.getTicket(), 2);

        }

        Assert.assertEquals("newpassword", userDAO.selectByID(1).getPassword());
        userDAO.deleteByID(1);
        Assert.assertNull(userDAO.selectByID(1));

//        Assert.assertEquals(1, loginTicketDAO.selectByTicket("TICKET1").getUserId());
//        Assert.assertEquals(2, loginTicketDAO.selectByTicket("TICKET1").getStatus());
    }

}