package cn.polister.service.impl;

import cn.polister.constants.LinkConstants;
import cn.polister.entity.Link;
import cn.polister.entity.ResponseResult;
import cn.polister.mapper.LinkMapper;
import cn.polister.service.LinkService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
}
