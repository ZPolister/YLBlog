package cn.polister.service;

import cn.polister.entity.Menu;
import cn.polister.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2024-01-22 15:41:50
 */
public interface MenuService extends IService<Menu> {

    List<Menu> getPermissionsByRoles(List<Role> roles);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);
}
