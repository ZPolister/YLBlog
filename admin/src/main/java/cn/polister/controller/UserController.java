package cn.polister.controller;

import cn.polister.entity.ResponseResult;
import cn.polister.entity.dto.UserDto;
import cn.polister.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Resource
    private UserService userService;
    @GetMapping("/list")
    public ResponseResult listByPage(Integer pageNum, Integer pageSize,
                                     String userName, String phonenumber, String status) {
        return userService.listByPage(pageNum, pageSize, userName, phonenumber, status);
    }

    @PostMapping
    public ResponseResult addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getUserInfo(@PathVariable Long id) {
        return userService.getUserInfo(id);
    }

    @PutMapping()
    public ResponseResult updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }
}
