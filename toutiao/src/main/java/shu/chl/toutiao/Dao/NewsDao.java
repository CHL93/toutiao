package shu.chl.toutiao.Dao;

import org.apache.ibatis.annotations.*;
import shu.chl.toutiao.Bean.News;

import java.util.List;

@Mapper
public interface NewsDao {
    String TABLE_NAME="news";
    String INSERT_FIELDS=" title,link,image,like_count,comment_count,created_date,user_id";
    String SELECT_FIELDS="id,"+INSERT_FIELDS;
    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,")"," Values(#{title},#{link},#{image}," +
            "#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    public int addNews(News news);
    @Select({"Select",SELECT_FIELDS,"from",TABLE_NAME, "where id=#{newsId}"})
    public News selectByNewsId(@Param("newsId") int newsId);
    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit);
    @Update({"update",TABLE_NAME,"set comment_count=#{count} where id=#{newsId}"})
    int updateComment(@Param("newsId") int newsId, @Param("count") int count);

    @Update({"update",TABLE_NAME,"set like_count=#{likeCount} where id=#{newsId}"})
    int updateLikeCount(@Param("newsId") int newsId,@Param("likeCount") long likeCount);
}
