package cn.polister.service;

import cn.polister.entity.Category;
import cn.polister.entity.ResponseResult;
import cn.polister.entity.dto.CategoryDto;
import cn.polister.entity.dto.CategoryUpdateVo;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 分类表(Category)表服务接口
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult list(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(CategoryDto categoryDto);

    ResponseResult updateCategory(CategoryUpdateVo categoryUpdateVo);

    ResponseResult deleteCategory(Long id);

    ResponseResult getCategoryInfo(Long id);
}
