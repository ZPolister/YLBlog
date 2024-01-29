package cn.polister.service.impl;

import cn.polister.constants.SystemConstants;
import cn.polister.entity.Menu;
import cn.polister.entity.ResponseResult;
import cn.polister.entity.Role;
import cn.polister.entity.RoleMenu;
import cn.polister.entity.vo.MenuTreeVo;
import cn.polister.enums.AppHttpCodeEnum;
import cn.polister.mapper.MenuMapper;
import cn.polister.mapper.RoleMenuMapper;
import cn.polister.service.MenuService;
import cn.polister.service.RoleService;
import cn.polister.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2024-01-22 15:41:50
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    private RoleService roleService;

    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Override
    public List<Menu> getPermissionsByRoles(List<Role> roles) {
        // 先拿到对应的角色id
        List<Long> roleIds = roles.stream().map(Role::getId).toList();
        List<RoleMenu> roleMenus;
        // 如果为超级管理员
        if (roleIds.size() == 1 && roleIds.get(0) == 1L) {
            roleMenus = roleMenuMapper.selectList(null);
        } else { // 不是超级管理员
            LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(RoleMenu::getRoleId, roleIds);
            roleMenus = roleMenuMapper.selectList(queryWrapper);
        }
        // 再用id去关联表获取权限id
        List<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).distinct().toList();

        // 最后获取权限信息,拿到菜单类型为C或者F的，状态为正常的权限返回
        return this.listByIds(menuIds).stream().filter(menu ->
                SystemConstants.PERMISSION_STATUS_NORMAL.equals(menu.getStatus()) &&
                        (SystemConstants.MENU_TYPE_MENU.equals(menu.getMenuType()) ||
                                SystemConstants.MENU_TYPE_BUTTON.equals(menu.getMenuType()))).toList();
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        // 拿到用户的角色
        List<Role> roles = roleService.getRolesByUserId(userId);
        List<Long> roleIds = roles.stream().map(Role::getId).toList();
        List<RoleMenu> roleMenus;
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        // 按照菜单id升序
        queryWrapper.orderBy(true, true, RoleMenu::getMenuId);
        if (!userId.equals(1L) ) { // 不是超级管理员
            queryWrapper.in(RoleMenu::getRoleId, roleIds);
            // 拿到编号
            roleMenus = roleMenuMapper.selectList(queryWrapper);
            // 再用id去关联表获取权限id
            List<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).distinct().toList();
            // id符合的
            wrapper.in(Menu::getId, menuIds);
        }

        // 类型符合的
        wrapper.in(Menu::getMenuType, SystemConstants.MENU_TYPE_DIRECTORY, SystemConstants.MENU_TYPE_MENU);
        // 状态符合的
        wrapper.eq(Menu::getStatus, SystemConstants.PERMISSION_STATUS_NORMAL);
        // 升序
        wrapper.orderBy(true, true, Menu::getOrderNum);
        // 拿到结果
        List<Menu> menus = this.list(wrapper);
        return builderMenuTree(menus, 0L);
    }

    @Override
    public ResponseResult listAllMenus(String status, String menuName) {

        // 确定查找条件
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(status), Menu::getStatus, status);
        wrapper.like(StringUtils.hasText(menuName), Menu::getMenuName, menuName);

        // 拿到结果返回
        List<Menu> menus = this.list(wrapper);
        return ResponseResult.okResult(menus);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        this.save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenuInfo(Long id) {
        Menu menu = this.getById(id);
        return ResponseResult.okResult(menu);
    }

    @Override
    public ResponseResult updateMenuInfo(Menu menu) {

        if (menu.getParentId().equals(menu.getId())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,
                    "修改菜单' " + menu.getMenuName() +" '失败，上级菜单不能选择自己");
        }

        this.updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {

        List<Menu> menus = this.list();
        for (var menu : menus) {
            if (menu.getParentId().equals(id)) {
                return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "存在子菜单不允许删除");
            }
        }

        this.removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTreeSelect() {
        // 拿到所有状态正常的权限
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getStatus, SystemConstants.PERMISSION_STATUS_NORMAL);
        wrapper.orderByAsc(Menu::getOrderNum);
        List<Menu> menus = this.list(wrapper);

        // 建树
        List<Menu> result = builderMenuTree(menus, 0L);

        // 对树进行VO优化
        List<MenuTreeVo> treeVo = getTreeVo(result);
        return ResponseResult.okResult(treeVo);
    }

    private List<MenuTreeVo> getTreeVo(List<Menu> list) {
        return list.stream().map(menu -> BeanCopyUtils.copyBean(menu, MenuTreeVo.class)
                .setChildren(getTreeChildrenVo(menu.getChildren()))
                        .setLabel(menu.getMenuName()))
                .toList();
    }

    private List<MenuTreeVo> getTreeChildrenVo(List<Menu> list) {
        return list.stream().map(menu -> BeanCopyUtils.copyBean(menu, MenuTreeVo.class)
                .setChildren(getTreeChildrenVo(menu.getChildren()))
                        .setLabel(menu.getMenuName()))
                .toList();
    }

    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        return menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
    }


    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        return menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
    }
}
