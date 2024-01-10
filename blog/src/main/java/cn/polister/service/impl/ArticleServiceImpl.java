package cn.polister.service.impl;

import cn.polister.constants.ArticleConstants;
import cn.polister.constants.HotArticleConstants;
import cn.polister.domain.ResponseResult;
import cn.polister.domain.entity.Article;
import cn.polister.domain.vo.ArticlePageVo;
import cn.polister.domain.vo.HotArticle;
import cn.polister.domain.vo.PageVo;
import cn.polister.entity.Category;
import cn.polister.mapper.ArticleMapper;
import cn.polister.mapper.CategoryMapper;
import cn.polister.service.ArticleService;
import cn.polister.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    final
    CategoryMapper categoryMapper;

    public ArticleServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public ResponseResult getHotArticleList() {

        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();

        // 已发布文章
        wrapper.eq(Article::getStatus, ArticleConstants.ARTICLE_STATUS_NORMAL);
        // 按照浏览量降序
        wrapper.orderByDesc(Article::getViewCount);
        // 最多10条，这里采用MBP的分页
        // 先拿到最热门的十篇文章
        Page<Article> page = new Page<>(1, HotArticleConstants.HOT_ARTICLE_NUMBER);
        page(page, wrapper);
        // 从数据库中获得数据
        List<Article> records = page.getRecords();
        // 对数据进行VO优化
        return ResponseResult.okResult(BeanCopyUtils.copyBeanList(records, HotArticle.class));
    }

    @Override
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();

        // 已发布文章
        wrapper.eq(Article::getStatus, ArticleConstants.ARTICLE_STATUS_NORMAL);
        wrapper.orderByDesc(Article::getIsTop);

        // 如果传参中含有目录，为分类查询
        if (Objects.nonNull(categoryId) && categoryId > 0) {
            wrapper.eq(Article::getCategoryId, categoryId);
        }

        // 设置分页
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);
        // 获取结果
        List<Article> records = page.getRecords();

        // 获取分类表缓存
        List<Category> list = categoryMapper.selectList(null);
        Map<Long, String> idNameMap = new HashMap<>();
        list.forEach(o -> idNameMap.put(o.getId(), o.getName()));
        // 注入分类名
        records.forEach(o -> o.setCategoryName(idNameMap.get(o.getCategoryId())));
        // vo优化
        List<ArticlePageVo> articlePageVos = BeanCopyUtils.copyBeanList(records, ArticlePageVo.class);

        return ResponseResult.okResult(new PageVo(articlePageVos, page.getTotal()));
    }
}
