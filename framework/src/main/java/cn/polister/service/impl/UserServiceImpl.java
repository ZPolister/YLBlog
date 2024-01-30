package cn.polister.service.impl;

import cn.polister.constants.FrameworkSystemConstants;
import cn.polister.entity.*;
import cn.polister.entity.dto.UserDto;
import cn.polister.entity.vo.PageVo;
import cn.polister.entity.vo.UserInfoVo;
import cn.polister.entity.vo.UserInfoWithRolesVo;
import cn.polister.enums.AppHttpCodeEnum;
import cn.polister.exception.SystemException;
import cn.polister.mapper.RoleMapper;
import cn.polister.mapper.UserMapper;
import cn.polister.mapper.UserRoleMapper;
import cn.polister.service.UserService;
import cn.polister.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2024-01-13 13:29:16
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IService<User>, UserService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleMapper roleMapper;

    @Override
    public ResponseResult userInfo() {
        // 获取已经登录的用户
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 用UserID去数据库中获取信息
        User user = getById(loginUser.getUser().getId());

        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult userInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        userInfoNoNullCheck(user);
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        // 对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listByPage(Integer pageNum, Integer pageSize,
                                     String userName, String phonenumber, String status) {

        // 构造查找条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(status), User::getStatus, status);
        wrapper.like(StringUtils.hasText(userName), User::getUserName, userName);
        wrapper.like(StringUtils.hasText(phonenumber), User::getPhoneNumber, phonenumber);

        // 构造分页
        Page<User> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);

        return ResponseResult.okResult(new PageVo(page.getRecords(), page.getTotal()));
    }

    @Override
    public ResponseResult addUser(UserDto userDto) {
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        ResponseResult registered = register(user);

        List<UserRole> userRoles = userDto.getRoleIds().stream()
                .map(roleId -> new UserRole(user.getId(), roleId)).toList();

        userRoleMapper.insertList(userRoles);
        return registered;
    }

    @Override
    public ResponseResult deleteUser(Long id) {
        // 先检查是不是自己
        LoginUser loginUser = (LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loginUser.getUser().getId().equals(id)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "不能删除自己！");
        } else if (Long.valueOf(1L).equals(id)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }

        this.removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserInfo(Long id) {
        // 拿到基本信息
        User user = this.getById(id);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        // 拿到关联数据
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, id);
        List<UserRole> userRoles = userRoleMapper.selectList(wrapper);
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).distinct().toList();

        // 拿到所有角色列表
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getStatus, FrameworkSystemConstants.ROLE_STATUS_NORMAL);
        List<Role> roles = roleMapper.selectList(roleLambdaQueryWrapper);

        return ResponseResult.okResult(new UserInfoWithRolesVo(roleIds, roles, userInfoVo));

    }

    @Override
    public ResponseResult updateUser(UserDto userDto) {
        // 如果被更新的是超级管理员,自己才能更新信息
        LoginUser loginUser = (LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDto.getId().equals(1L) && !loginUser.getUser().getId().equals(userDto.getId())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }

        // 更新自己
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        this.updateById(user);

        // 更新角色关联
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userDto.getId());
        userRoleMapper.delete(wrapper);

        if (!userDto.getRoleIds().isEmpty()) {
            List<UserRole> userRoles = userDto.getRoleIds().stream()
                    .map(roleId -> new UserRole(userDto.getId(), roleId)).toList();

            userRoleMapper.insertList(userRoles);
        }

        return ResponseResult.okResult();
    }

    private void userInfoNoNullCheck(User user) {
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNickName, nickName);
        User one = this.getOne(wrapper);

        return Objects.nonNull(one);
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, userName);
        User one = this.getOne(wrapper);

        return Objects.nonNull(one);
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        User one = this.getOne(wrapper);

        return Objects.nonNull(one);
    }
}
