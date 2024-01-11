package cn.polister.mapper;

import cn.polister.domain.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 分类表(Category)表数据库访问层
 *
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
