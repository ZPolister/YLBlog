package cn.polister.service.impl;

import cn.polister.entity.Role;
import cn.polister.entity.UserRole;
import cn.polister.mapper.RoleMapper;
import cn.polister.mapper.UserRoleMapper;
import cn.polister.service.RoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2024-01-22 15:38:37
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    UserRoleMapper userRoleMapper;
    @Override
    public List<Role> getRolesByUserId(Long id) {
        // 先从关联表角色id
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, id);
        List<UserRole> userRoles = userRoleMapper.selectList(queryWrapper);
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).distinct().toList();
        // 然后再去角色表找到所有角色
        return listByIds(roleIds);
    }
}
