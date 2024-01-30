package cn.polister.service.impl;

import cn.polister.constants.FrameworkSystemConstants;
import cn.polister.constants.LinkConstants;
import cn.polister.entity.Link;
import cn.polister.entity.ResponseResult;
import cn.polister.entity.vo.PageVo;
import cn.polister.mapper.LinkMapper;
import cn.polister.service.LinkService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2024-01-11 10:17:53
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Link::getStatus, LinkConstants.LINK_STATUS_PASSED);
        List<Link> list = list(wrapper);

        return  ResponseResult.okResult(list);
    }

    @Override
    public ResponseResult listLinkByPage(Integer pageNum, Integer pageSize, String name, String status) {

        // 构造查找条件
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(status), Link::getStatus, status);
        wrapper.eq(StringUtils.hasText(name), Link::getName, name);

        // 构建MBP分页
        Page<Link> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);

        return ResponseResult.okResult(new PageVo(page.getRecords(), page.getTotal()));
    }

    @Override
    public ResponseResult addLink(Link link) {
        this.save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLinkInfo(Long id) {
        Link link = this.getById(id);
        return ResponseResult.okResult(link);
    }

    @Override
    public ResponseResult updateLink(Link link) {
        this.updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLink(Long id) {
        this.removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeLinkStatus(Long id, String status) {
        Link link = new Link();
        link.setStatus(status);
        link.setId(id);

        this.updateById(link);
        return ResponseResult.okResult();
    }
}
