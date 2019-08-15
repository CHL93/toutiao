package shu.chl.toutiao.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shu.chl.toutiao.Bean.LoginTicket;
import shu.chl.toutiao.Bean.User;
import shu.chl.toutiao.Dao.LoginTicketDao;
import shu.chl.toutiao.Util.ToutiaoUtil;
import shu.chl.toutiao.service.LoginTicketService;
import shu.chl.toutiao.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/home")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    LoginTicketService loginTicketService;
    @RequestMapping("/reg")
    @ResponseBody
    public String register(Model model, @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value="rember", defaultValue = "0") int rememberme,
                            HttpServletResponse response){
        try {
            Map<String, Object> map = userService.addUser(username, password);

            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0)
                    cookie.setMaxAge(3600 * 24 * 5);
               response.addCookie(cookie);
               return ToutiaoUtil.getJSONString(0, "欢迎" + username);
            }else{
                return ToutiaoUtil.getJSONString(1, map);
            }
        }catch (Exception e){
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }
    }

    @RequestMapping(path="/login",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String logIn(Model model,@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value="rember", defaultValue = "0") int rememberme,HttpServletResponse response){
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0) {
                    cookie.setMaxAge(3600*24*5);
                }

                response.addCookie(cookie);
//
                return ToutiaoUtil.getJSONString(0, "登入成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            logger.error("登入异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "登入异常");
        }
    }

    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
