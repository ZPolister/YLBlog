package cn.polister.service;

import cn.polister.entity.ResponseResult;
import cn.polister.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2024-01-13 13:29:16
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult userInfo(User user);

    ResponseResult register(User user);
}
