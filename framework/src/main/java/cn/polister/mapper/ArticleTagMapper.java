package cn.polister.mapper;

import cn.polister.entity.ArticleTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2024-01-23 16:12:24
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}
