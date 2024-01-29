package cn.polister.mapper;

import cn.polister.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 角色和菜单关联表(RoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2024-01-22 16:25:36
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    List<RoleMenu> findMenuByIds(@Param("ids") List<Long> ids);

}
