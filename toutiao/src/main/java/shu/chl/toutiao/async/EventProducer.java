package shu.chl.toutiao.async;

/*
*    当一个业务发起需要异步处理的事件后，我们通过将事件的信息（EvenModel） 序列化存储到Redis中对应的队列。
*    producer做的
*
*    消费者则在判断异步处理事件队列（list）不为空的时候，将信息取出反序列化事件。交到对应的handler 处理
* */

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shu.chl.toutiao.Util.RedisAdapter;
import shu.chl.toutiao.Util.RedisKeyUtil;
import shu.chl.toutiao.async.EventModel;

@Service
public class EventProducer {
    @Autowired
    RedisAdapter redisAdapter;

    public boolean fireEvent(EventModel eventModel){
        try{
            String json= JSONObject.toJSONString(eventModel);
            String key= RedisKeyUtil.getEventQueueKey();
             redisAdapter.lpush(key,json);
             return true;
        }catch(Exception e){}
          return false;
    }
}
