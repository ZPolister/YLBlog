package cn.polister.service.impl;

import cn.polister.entity.LoginUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
    public boolean hasPermission(String permission) {

        // 拿到用户信息
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // admin
        if (loginUser.getUser().getId().equals(1L)) {
            return true;
        }

        // 不是admin
        return loginUser.getPermissions().contains(permission);
    }
}
