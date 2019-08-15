package shu.chl.toutiao.Bean;

import org.springframework.stereotype.Component;
/*
* 模仿spring的IOC,对象管理交给
* */
@Component
public class UserHolder {
    private  ThreadLocal<User> users=new ThreadLocal<>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
         users.set(user);
    }
    public void clear() {
        users.remove();;
    }
}
