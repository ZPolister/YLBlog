package cn.polister.mapper;

import cn.polister.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2024-01-11 11:35:48
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
        List<User> select(String username);
}
