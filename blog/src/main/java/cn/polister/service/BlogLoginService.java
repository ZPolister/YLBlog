package cn.polister.service;

import cn.polister.entity.ResponseResult;
import cn.polister.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
