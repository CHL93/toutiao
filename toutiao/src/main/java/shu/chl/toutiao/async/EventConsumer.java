package shu.chl.toutiao.async;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import shu.chl.toutiao.Util.RedisAdapter;
import shu.chl.toutiao.Util.RedisKeyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    //根据相应的事件类型找到对应的Handler
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    RedisAdapter redisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
            //IOC容器初始化bean之后，扫描hangder
        System.out.println("---------------------------");
           Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
            if(beans!=null){
                for(Map.Entry<String,EventHandler> entry:beans.entrySet()){
                    List<EventType> eventTypes=entry.getValue().getSupportEventTypes();
                    for(EventType type:eventTypes){
                        if(!config.containsKey(type)){
                            config.put(type,new ArrayList<>());
                        }

                        //一个事件可以对应好几个Hangdler.
                        config.get(type).add(entry.getValue());
                    }
                }
            }
            //启动线程去消费事件
        Thread  thread=new Thread(new Runnable() {

            @Override
            public void run() {
               while(true){
                   String key= RedisKeyUtil.getEventQueueKey();
                   // 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，
                   //第二个元素是被弹出元素的值。
                   List<String> messages=redisAdapter.brpop(0,key);
                   System.out.println("ddf");

                   for(String message:messages){
                       if(message.equals(key))
                           continue;
                   EventModel eventModel= JSON.parseObject(message,EventModel.class);
                   if(!config.containsKey(eventModel.getType())){
                       logger.error("不能识别的事件");
                       continue;
                   }

                   for(EventHandler handler : config.get(eventModel.getType())){
                       handler.doHandle(eventModel);
                   }
               }
               }

            }
        });
            thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
