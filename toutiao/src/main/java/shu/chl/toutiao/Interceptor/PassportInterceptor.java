package shu.chl.toutiao.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import shu.chl.toutiao.Bean.LoginTicket;
import shu.chl.toutiao.Bean.UserHolder;
import shu.chl.toutiao.service.LoginTicketService;
import shu.chl.toutiao.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/*
*    postHander 验证用户在进入网站时是否已经登入过,获取ticket.
*    写完通过springBoot进行配置* */
@Component
public class PassportInterceptor  implements HandlerInterceptor{
    @Autowired
    LoginTicketService loginTicketService;
    @Autowired
    UserService userService;
    @Autowired
    UserHolder userHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
          Cookie[] cookies= request.getCookies();
          String ticket=null;
          if(cookies!=null) {
              for (Cookie cookie : cookies) {
                  if (cookie.getName().equals("ticket")) {
                      ticket = cookie.getValue();
                      break;
                  }
              }
          }
              LoginTicket loginTicket=loginTicketService.selectTicket(ticket);
          if(loginTicket==null || loginTicket.getStatus()!=0 || loginTicket.getExpired().before(new Date()))
              return true;

           //如果有效
           userHolder.setUser(userService.selectByID(loginTicket.getUserId()));

         return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
       //在渲染之前
        if(modelAndView!=null && userHolder.getUser()!=null){
            modelAndView.addObject("user",userHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
         //退出程序后
        userHolder.clear();
    }
}
