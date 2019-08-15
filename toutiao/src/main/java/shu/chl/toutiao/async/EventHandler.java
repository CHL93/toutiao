package shu.chl.toutiao.async;


import java.util.List;

/*
*
* 用于处理特定的事件*/
public interface EventHandler {
    //处理事件核心函数
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}

