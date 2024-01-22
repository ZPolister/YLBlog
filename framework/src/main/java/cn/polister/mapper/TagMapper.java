package cn.polister.mapper;

import cn.polister.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2024-01-22 12:53:58
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}
