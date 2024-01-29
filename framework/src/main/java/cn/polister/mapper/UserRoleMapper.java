package cn.polister.mapper;

import cn.polister.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户和角色关联表(UserRole)表数据库访问层
 *
 * @author makejava
 * @since 2024-01-22 16:07:05
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
