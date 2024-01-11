package cn.polister.service.impl;

import cn.polister.entity.LoginUser;
import cn.polister.entity.User;
import cn.polister.mapper.UserMapper;
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
    UserMapper userMapper;

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

        //TODO 查询用户信息

        return new LoginUser(user);
    }
}
