package cn.polister.mapper;

import cn.polister.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2024-01-22 15:38:34
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}
