package shu.chl.toutiao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shu.chl.toutiao.Bean.Message;
import shu.chl.toutiao.Dao.MessageDao;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDao messageDao;
    public int addMessage(Message message){
        int fromId=message.getFromId();
        int toId=message.getToId();
        String conversionId=fromId>toId?toId+"_"+fromId:fromId+"_"+toId;
        message.setConversationId(conversionId);
        return messageDao.addMessage(message);
    }

    public List<Message> getConversionList(int userId,int offset,int limit){
        return  messageDao.getConversationList(userId,offset,limit);

    }
    public int getUnreadCount(String conversationId){
        return  messageDao.getUnreadCount(conversationId);
    }
    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDao.getConversationDetail(conversationId,offset,limit);
    }
}
