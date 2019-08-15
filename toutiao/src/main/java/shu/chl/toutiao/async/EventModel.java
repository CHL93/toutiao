package shu.chl.toutiao.async;

import java.util.HashMap;
import java.util.Map;

/*
* 记录每个事件的关键信息：
*                  事件类型
*                  发起事件人的id
*                   事件对应类的类型
*                      对应类的id
*                      该类的拥有者
*                 比如对我发布的新闻点赞。   事件类型 是like,   发起事件人 是点赞的。     由发起的类型引发的事件：新闻， 新闻id. 我是发布新闻的人
* */
public class EventModel {
    private EventType type;
    private int actorId;
    private int entityId;
    private int entityType;
    private int entityOwnerId;
    private Map<String,String> exts=new HashMap<>();  //纪录额外信息

    public EventModel(){

    }
    public EventModel(EventType eventType){
        this.type=eventType;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public void setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }
}
