package cn.polister.service.impl;

import cn.polister.entity.ResponseResult;
import cn.polister.enums.AppHttpCodeEnum;
import cn.polister.exception.SystemException;
import cn.polister.service.UploadService;
import cn.polister.utils.CosClient;
import cn.polister.utils.PathUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class UploadServiceImpl implements UploadService {

    @Resource
    private CosClient cosClient;
    @Override
    public ResponseResult upload(MultipartFile img) {

        // 验证文件后缀是否正确
        String originalFilename = img.getOriginalFilename();
        if (originalFilename == null
                || (!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")))
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);


        String url = null;
        try {
            url = cosClient.upLoadFile(img, PathUtils.generateFilePath(originalFilename));
        } catch (IOException e) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult(url);
    }
}
