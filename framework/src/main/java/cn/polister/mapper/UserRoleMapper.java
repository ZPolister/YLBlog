package cn.polister.mapper;

import cn.polister.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 用户和角色关联表(UserRole)表数据库访问层
 *
 * @author makejava
 * @since 2024-01-22 16:07:05
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    void insertList(@Param("list") List<UserRole> list);

}
