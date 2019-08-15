package shu.chl.toutiao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shu.chl.toutiao.Bean.LoginTicket;
import shu.chl.toutiao.Bean.User;
import shu.chl.toutiao.Dao.LoginTicketDao;
import shu.chl.toutiao.Dao.UserDao;
import  org.apache.commons.lang3.StringUtils;
import shu.chl.toutiao.Util.ToutiaoUtil;



import java.util.*;

@Service
public class UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    LoginTicketDao loginTicketDao;

    public Map<String,Object> addUser(String username,String password){
          Map<String,Object> map=new HashMap<>();
          if(StringUtils.isBlank(username)){
               map.put("nameMSG","用户名为空");
               return  map;
          }
          if(StringUtils.isBlank(password)){
              map.put("passwordMSG","密码为空");
              return map;
          }
          User user1=userDao.selectByName(username);
          if(user1!=null){
              map.put("nameMSG","用户名已存在");
              return map;
          }
        User user=new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        userDao.addUser(user);
        return  map;
    }
     public Map<String,Object> login(String username,String password){
         Map<String, Object> map = new HashMap<String, Object>();
         if (StringUtils.isBlank(username)) {
             map.put("msgname", "用户名不能为空");
             return map;
         }

         if (StringUtils.isBlank(password)) {
             map.put("msgpwd", "密码不能为空");
             return map;
         }

         User user = userDao.selectByName(username);

         if (user == null) {
             map.put("msgname", "用户名不存在");
             return map;
         }

         if (!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
             map.put("msgpwd", "密码不正确");
             return map;
         }

         String ticket = addLoginTicket(user.getId());
         map.put("ticket", ticket);
         return map;
     }
    public void logout(String ticket) {
        loginTicketDao.updateStatus(ticket, 1);
    }

    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDao.addTicket(ticket);
        return ticket.getTicket();
    }


    public User selectByID(int userId){
       return userDao.selectByID(userId);
    }

    public void updatePassword(User user){
        userDao.updatePassword(user);
    }


}
