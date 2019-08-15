package shu.chl.toutiao.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shu.chl.toutiao.Bean.Message;
import shu.chl.toutiao.Bean.User;
import shu.chl.toutiao.Bean.UserHolder;
import shu.chl.toutiao.Bean.ViewObject;
import shu.chl.toutiao.service.MessageService;
import shu.chl.toutiao.service.UserService;

import javax.swing.text.html.ObjectView;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    UserHolder userHolder;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @RequestMapping("/msg/list")
    public String getMessageList(Model model){
        int localId=userHolder.getUser().getId();
        List<ViewObject> vos=new ArrayList<ViewObject>();
        List<Message> conversions=messageService.getConversionList(localId,0,10);
        for(Message conversion:conversions){
            User fromUser=userService.selectByID(conversion.getFromId());
            int unReadCount=messageService.getUnreadCount(conversion.getConversationId());
            ViewObject vo=new ViewObject();
            vo.set("conversation",conversion);
            vo.set("fromUser",fromUser);
            vo.set("unReadCount",unReadCount);
            vos.add(vo);

        }
         model.addAttribute("conversations",vos);

        return "letter";
    }
    @RequestMapping("/msg/detail")
    public String getMessageDetail(@RequestParam("conversationId") String conversationId,Model model){
        List<Message> messageList=messageService.getConversationDetail(conversationId,0,10);
        List<ViewObject> vos=new ArrayList<>();
        for(Message message:messageList){
              User fromUeser=userService.selectByID(message.getFromId());
              ViewObject vo=new ViewObject();
              vo.set("fromUser",fromUeser);
              vo.set("message",message);
              vos.add(vo);
        }
        System.out.println(messageList.size());
        System.out.println(messageList.get(0).getCreatedDate());
        model.addAttribute("messageList",vos);
        
        return "letterDetail";
    }


}
