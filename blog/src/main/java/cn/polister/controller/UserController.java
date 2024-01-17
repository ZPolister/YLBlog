package cn.polister.controller;

import cn.polister.entity.ResponseResult;
import cn.polister.entity.User;
import cn.polister.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/userInfo")
    ResponseResult userInfo() {
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    ResponseResult userInfo(@RequestBody User user) {
        return userService.userInfo(user);
    }

    @PostMapping("/register")
    ResponseResult register(@RequestBody User user) {
        return userService.register(user);
    }
}
