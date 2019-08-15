package shu.chl.toutiao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shu.chl.toutiao.Bean.LoginTicket;
import shu.chl.toutiao.Bean.User;
import shu.chl.toutiao.Dao.LoginTicketDao;
import shu.chl.toutiao.Dao.UserDao;

@Service
public class LoginTicketService {
    @Autowired
    private LoginTicketDao loginTicketDao;
    @Autowired
    private UserDao userDao;

    public LoginTicket selectTicket(String ticket){
        LoginTicket loginTicket=loginTicketDao.selectByTicket(ticket);

        return loginTicket;
    }
}
