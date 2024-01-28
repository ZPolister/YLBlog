package cn.polister.service.impl;

import cn.polister.entity.*;
import cn.polister.mapper.MenuMapper;
import cn.polister.mapper.RoleMenuMapper;
import cn.polister.mapper.UserMapper;
import cn.polister.mapper.UserRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 先从数据库里查找用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(wrapper);
        //List<User> select = userMapper.select(username);
        // 用户名不存在的情况
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在！");
        }

        // 查询用户信息
        List<String> permissions;
        if (user.getId().equals(1L)) {
            permissions = menuMapper.selectList(null)
                    .stream().map(Menu::getPerms).toList();
        } else {
            LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userRoleLambdaQueryWrapper.eq(UserRole::getUserId, user.getId());
            List<UserRole> userRoles = userRoleMapper.selectList(userRoleLambdaQueryWrapper);
            List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).toList();

            LambdaQueryWrapper<RoleMenu> roleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roleMenuLambdaQueryWrapper.in(RoleMenu::getRoleId, roleIds);
            List<RoleMenu> roleMenus = roleMenuMapper.selectList(roleMenuLambdaQueryWrapper);
            List<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).toList();

            LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            menuLambdaQueryWrapper.in(Menu::getId, menuIds);
            List<Menu> menus = menuMapper.selectList(menuLambdaQueryWrapper);
            permissions = menus.stream().map(Menu::getPerms).toList();
        }

        return new LoginUser(user, null, permissions);
    }
}
