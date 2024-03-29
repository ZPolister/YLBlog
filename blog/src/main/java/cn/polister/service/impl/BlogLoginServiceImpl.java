package cn.polister.service.impl;

import cn.polister.constants.FrameworkSystemConstants;
import cn.polister.entity.vo.BlogUserLoginVo;
import cn.polister.entity.vo.UserInfoVo;
import cn.polister.entity.LoginUser;
import cn.polister.entity.ResponseResult;
import cn.polister.entity.User;
import cn.polister.enums.AppHttpCodeEnum;
import cn.polister.service.BlogLoginService;
import cn.polister.utils.BeanCopyUtils;
import cn.polister.utils.JwtUtil;
import cn.polister.utils.RedisCache;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Resource
    RedisCache redisCache;

    @Resource
    AuthenticationManager authenticationManager;

    @Override
    public ResponseResult login(User user) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 登录失败
        if (Objects.isNull(authenticate)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR, "用户名或密码错误");
        }

        // 生成JWT
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String jwt = JwtUtil.createJWT(loginUser.getUser().getId().toString());
        loginUser.setToken(jwt);

        // 缓存到Redis中
        redisCache.setCacheObject("BlogLogin:" + jwt, loginUser,
                FrameworkSystemConstants.LOGIN_TIMEOUT, TimeUnit.DAYS);

        // 封装Vo返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        return ResponseResult.okResult(new BlogUserLoginVo(jwt, userInfoVo));

    }

    @Override
    public ResponseResult logout() {

        // 从ContextHolder中获得LoginUser
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        // 移除redis上缓存
        redisCache.deleteObject("BlogLogin:" + loginUser.getToken());

        return ResponseResult.okResult();
    }
}