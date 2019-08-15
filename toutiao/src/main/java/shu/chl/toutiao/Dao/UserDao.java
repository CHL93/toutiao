package shu.chl.toutiao.Dao;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import shu.chl.toutiao.Bean.User;

@Mapper
public interface UserDao {
    String TABLE_NAME="user";
    String INSERT_FIELDS=" name, password, salt, head_url ";
    String SELECT_FIELDS=" id, name, password, salt, head_url ";
    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,")","VALUES(#{name},#{password},#{salt},#{headUrl})"})
    public int addUser(User user);

    @Select({"select",SELECT_FIELDS,"from ",TABLE_NAME,"where id=#{id}"})
    public User selectByID(int id);
    @Select({"select",SELECT_FIELDS,"form ",TABLE_NAME," where name=#{name} and password=#{password}" })
    User login(String name,String password);
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where name=#{name}"})
    public User selectByName(String name);

    @Update({"update ",TABLE_NAME," set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Delete({"delete from ",TABLE_NAME," where id=#{id}"})
    public void deleteByID(int id);
}
