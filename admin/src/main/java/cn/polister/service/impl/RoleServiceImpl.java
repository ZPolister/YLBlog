package cn.polister.service.impl;

import cn.polister.constants.FrameworkSystemConstants;
import cn.polister.entity.ResponseResult;
import cn.polister.entity.Role;
import cn.polister.entity.RoleMenu;
import cn.polister.entity.UserRole;
import cn.polister.entity.dto.RoleAddDto;
import cn.polister.entity.dto.RoleChangeStatusDto;
import cn.polister.entity.vo.RoleListVo;
import cn.polister.entity.vo.RolePageVo;
import cn.polister.mapper.RoleMapper;
import cn.polister.mapper.RoleMenuMapper;
import cn.polister.mapper.UserRoleMapper;
import cn.polister.service.RoleService;
import cn.polister.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;
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

    @Override
    public ResponseResult listRoleByPage(Integer pageNum, Integer pageSize, String roleName, String status) {

        // 构建查找条件
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(status), Role::getStatus, status);
        wrapper.like(StringUtils.hasText(roleName), Role::getRoleName, roleName);
        wrapper.orderByAsc(Role::getRoleSort);

        // 构建分页
        Page<Role> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);

        // 获取数据封装返回
        List<Role> records = page.getRecords();
        List<RoleListVo> roleListVos = BeanCopyUtils.copyBeanList(records, RoleListVo.class);

        return ResponseResult.okResult(new RolePageVo(roleListVos, page.getTotal()));

    }

    @Override
    public ResponseResult changeRoleStatus(RoleChangeStatusDto roleChangeStatusDto) {
        Role role = new Role();
        role.setId(roleChangeStatusDto.getRoleId());
        role.setStatus(roleChangeStatusDto.getStatus());

        this.updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addRole(RoleAddDto roleAddDto) {

        Role role = BeanCopyUtils.copyBean(roleAddDto, Role.class);
        this.save(role);

        // 保存菜单
        List<RoleMenu> roleMenus = roleAddDto.getMenuIds()
                .stream().map(r -> new RoleMenu(role.getId(), r)).toList();

        roleMenuMapper.insertList(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleInfo(Long id) {
        Role role = this.getById(id);
        return ResponseResult.okResult(role);
    }

    @Override
    public ResponseResult updateRoleInfo(RoleAddDto roleAddDto) {
        // 先对角色信息本身进行更新
        Role role = BeanCopyUtils.copyBean(roleAddDto, Role.class);
        this.updateById(role);

        // 再对关联表id进行更新
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, role.getId());
        roleMenuMapper.delete(wrapper);

        // 最后再对新的关联id进行插入
        List<RoleMenu> roleMenus = roleAddDto.getMenuIds().stream()
                .map(menuId -> new RoleMenu(role.getId(), menuId)).toList();
        roleMenuMapper.insertList(roleMenus);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRole(Long id) {
        // 先从关联表中删了该角色
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, id);
        roleMenuMapper.delete(wrapper);

        // 删除本体
        this.removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, FrameworkSystemConstants.ROLE_STATUS_NORMAL);

        List<Role> roles = this.list(wrapper);
        return ResponseResult.okResult(roles);
    }


}
