package cn.polister.service;

import cn.polister.entity.Link;
import cn.polister.entity.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2024-01-11 10:17:53
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();
}
