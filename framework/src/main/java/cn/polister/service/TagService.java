package cn.polister.service;

import cn.polister.entity.ResponseResult;
import cn.polister.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2024-01-22 12:53:58
 */
public interface TagService extends IService<Tag> {

    ResponseResult listByPage(Integer pageNum, Integer pageSize, String name, String remark);

    ResponseResult addTag(Tag tag);

    ResponseResult deleteTag(Long id);

    ResponseResult getTag(Long id);
}
