package cn.polister.service;

import cn.polister.entity.Category;
import cn.polister.entity.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 分类表(Category)表服务接口
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}
