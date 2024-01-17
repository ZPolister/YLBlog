package cn.polister.controller;

import cn.polister.entity.ResponseResult;
import cn.polister.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class upLoadController {

    @Resource
    private UploadService uploadService;
    @PostMapping("/upload")
    ResponseResult upload(MultipartFile img) {
        return uploadService.upload(img);
    }
}
