package cn.polister.controller;

import cn.polister.entity.ResponseResult;
import cn.polister.entity.User;
import cn.polister.service.BlogLoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/")
public class BlogLoginController {

    @Resource
    BlogLoginService blogLoginService;
    @PostMapping("/login")
    ResponseResult login(@RequestBody User user) {
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    ResponseResult logout() {
        return blogLoginService.logout();
    }
}
