package shu.chl.toutiao.async.Handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shu.chl.toutiao.Bean.Message;
import shu.chl.toutiao.Bean.User;
import shu.chl.toutiao.async.EventHandler;
import shu.chl.toutiao.async.EventModel;
import shu.chl.toutiao.async.EventType;
import shu.chl.toutiao.service.MessageService;
import shu.chl.toutiao.service.UserService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        //要向内容发送这发送者发私信，有人给你点赞了
        int actorId=model.getActorId();
        int ownerId=model.getEntityOwnerId();
        Message message=new Message();
        User user=userService.selectByID(actorId);
        message.setContent(user.getName()+"想你点了赞");
        message.setFromId(actorId);
        message.setToId(ownerId);
        message.setCreatedDate(new Date());
        System.out.println("开始执行了 00000000000000000");
        messageService.addMessage(message);


    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
