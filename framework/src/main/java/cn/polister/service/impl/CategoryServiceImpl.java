package cn.polister.service.impl;

import cn.polister.constants.ArticleConstants;
import cn.polister.constants.CategoryConstants;
import cn.polister.entity.Category;
import cn.polister.entity.dto.CategoryDto;
import cn.polister.entity.dto.CategoryUpdateVo;
import cn.polister.entity.vo.CategoryListVo;
import cn.polister.entity.vo.CategoryPageVo;
import cn.polister.entity.vo.CategoryShowVo;
import cn.polister.entity.Article;
import cn.polister.entity.ResponseResult;
import cn.polister.mapper.CategoryMapper;
import cn.polister.service.ArticleService;
import cn.polister.service.CategoryService;
import cn.polister.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2024-01-10 16:12:39
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final ArticleService articleService;

    public CategoryServiceImpl(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    public ResponseResult getCategoryList() {

        // 先拿到所有文章，将文章id列出来去重
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, ArticleConstants.ARTICLE_STATUS_NORMAL);
        List<Article> list = articleService.list(wrapper);
        Set<Long> categoryId = list.stream().map(Article::getCategoryId).collect(Collectors.toSet());

        // 然后用这些id去获得分类信息
        List<Category> categories = listByIds(categoryId);

        // 再判断分类内有没有文章，将没有文章的分类去掉
        List<Category> result = categories.stream()
                .filter(o -> o.getStatus().equals(CategoryConstants.CATEGORY_STATUS_NORMAL))
                .toList();

        // 进行vo优化
        return ResponseResult.okResult(BeanCopyUtils.copyBeanList(result, CategoryShowVo.class));
    }

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, String name, String status) {

        // 构造查找条件
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(status), Category::getStatus, status);
        wrapper.like(StringUtils.hasText(name), Category::getName, name);

        // 构造分页
        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);

        // 获取结果返回
        List<Category> records = page.getRecords();
        List<CategoryListVo> result = BeanCopyUtils.copyBeanList(records, CategoryListVo.class);
        return ResponseResult.okResult(new CategoryPageVo(result, page.getTotal()));
    }

    @Override
    public ResponseResult addCategory(CategoryDto categoryDto) {

        // 进行beanCopy
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);

        // 保存到数据库
        this.save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateCategory(CategoryUpdateVo categoryUpdateVo) {
        Category category = BeanCopyUtils.copyBean(categoryUpdateVo, Category.class);

        this.updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategory(Long id) {

        this.removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryInfo(Long id) {
        Category category = this.getById(id);
        return ResponseResult.okResult(category);
    }
}
