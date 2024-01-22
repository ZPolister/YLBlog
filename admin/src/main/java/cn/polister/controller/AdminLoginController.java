package cn.polister.controller;

import cn.polister.entity.*;
import cn.polister.entity.vo.AdminUserInfoVo;
import cn.polister.entity.vo.AdminUserVo;
import cn.polister.entity.vo.UserInfoVo;
import cn.polister.service.AdminLoginService;
import cn.polister.service.MenuService;
import cn.polister.service.RoleService;
import cn.polister.utils.BeanCopyUtils;
import com.qcloud.cos.model.ciModel.auditing.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@RestController
public class AdminLoginController {

    @Resource
    private RoleService roleService;
    @Resource
    private MenuService menuService;
    @Resource
    private AdminLoginService adminLoginService;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        return adminLoginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult getInfo() {
        // 拿到User信息
        LoginUser loginUser = (LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 获取角色信息
        List<Role> roles = roleService.getRolesByUserId(loginUser.getUser().getId());
        List<String> rolesKeys = roles.stream().map(Role::getRoleKey).distinct().toList();
        // 获取权限信息
        List<Menu> menus = menuService.getPermissionsByRoles(roles);
        List<String> menusPerms = menus.stream().map(Menu::getPerms).distinct().toList();
        // 获取用户信息
        UserInfoVo userInfo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);

        return ResponseResult.okResult(new AdminUserInfoVo(menusPerms, rolesKeys, userInfo));
    }

    @GetMapping("/getRouters")
    public ResponseResult getRouters(){
        LoginUser loginUser = (LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getUser().getId();
        //查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        HashMap<String, List<Menu>> result = new HashMap<>();
        result.put("menus", menus);
        return ResponseResult.okResult(result);
    }

    @PostMapping("/user/logout")
    ResponseResult logout() {
        return adminLoginService.logout();
    }
}
