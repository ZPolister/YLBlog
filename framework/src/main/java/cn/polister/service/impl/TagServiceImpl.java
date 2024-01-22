package cn.polister.service.impl;

import cn.polister.entity.ResponseResult;
import cn.polister.entity.Tag;
import cn.polister.entity.vo.PageVo;
import cn.polister.mapper.TagMapper;
import cn.polister.service.TagService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2024-01-22 12:53:58
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult listByPage(Integer pageNum, Integer pageSize, String name, String remark) {
        // 创建分页对象
        Page<Tag> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(name), Tag::getName, name);
        wrapper.eq(StringUtils.hasText(remark), Tag::getRemark, remark);

        page(page, wrapper);
        List<Tag> records = page.getRecords();
        long total = page.getTotal();
        PageVo pageVo = new PageVo(records, total);

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(Tag tag) {
        this.save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        this.removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTag(Long id) {
        return ResponseResult.okResult(this.getById(id));
    }
}
