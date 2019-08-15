package shu.chl.toutiao.Dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import shu.chl.toutiao.Bean.Message;

import java.util.List;

@Mapper
public interface MessageDao {
    String TABLE_NAME="message";
    String INSERT_FIELDS=" from_id,to_id,content,created_date,has_read,conversation_id";
    String SELECR_FIELDS=" id ,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,")"," Values(#{fromId},#{toId},#{content}," +
            "#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);

    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationList(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);
    @Select({"select count(*) as count from",TABLE_NAME,"where conversation_id=#{conversationId} and has_read=0"})
    int getUnreadCount(@Param("conversationId") String conversationId);

    @Select({"select ", INSERT_FIELDS, "from ", TABLE_NAME, " where conversation_id=#{conversation_id} order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("conversation_id") String conversation_id, @Param("offset") int offset, @Param("limit") int limit);


}
