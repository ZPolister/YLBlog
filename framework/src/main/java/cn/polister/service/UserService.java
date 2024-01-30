package cn.polister.service;

import cn.polister.entity.ResponseResult;
import cn.polister.entity.User;
import cn.polister.entity.dto.UserDto;
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


    ResponseResult listByPage(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult addUser(UserDto userDto);

    ResponseResult deleteUser(Long id);

    ResponseResult getUserInfo(Long id);

    ResponseResult updateUser(UserDto userDto);
}
