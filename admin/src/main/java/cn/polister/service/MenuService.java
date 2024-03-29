package cn.polister.service;

import cn.polister.entity.Menu;
import cn.polister.entity.ResponseResult;
import cn.polister.entity.Role;
import cn.polister.entity.vo.MenuTreeVo;
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

    ResponseResult listAllMenus(String status, String menuName);

    ResponseResult addMenu(Menu menu);

    ResponseResult getMenuInfo(Long id);

    ResponseResult updateMenuInfo(Menu menu);

    ResponseResult deleteMenu(Long id);

    List<MenuTreeVo> getTreeSelect();

    ResponseResult getTreeSelectByRole(Long id);
}
