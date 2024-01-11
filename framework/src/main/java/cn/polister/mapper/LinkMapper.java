package cn.polister.mapper;

import cn.polister.entity.Link;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 友链(Link)表数据库访问层
 *
 * @author makejava
 * @since 2024-01-11 10:17:51
 */
@Mapper
public interface LinkMapper extends BaseMapper<Link> {

}
