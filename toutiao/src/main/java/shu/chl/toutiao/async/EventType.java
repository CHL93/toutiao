package shu.chl.toutiao.async;
/*
*
*  EventType 用来记录事件类型，根据事件类型找到处理事件对应的handler处理
* */
public enum  EventType {
    LIKE(0),COMENT(1),LOGIN(2),MAIL(3);
     private int value;
     EventType(int value){
         this.value=value;
     }
     public int getValue(){
         return value;
     }
}
