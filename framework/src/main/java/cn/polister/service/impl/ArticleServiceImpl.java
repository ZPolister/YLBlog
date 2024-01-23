package cn.polister.service.impl;

import cn.polister.constants.ArticleConstants;
import cn.polister.constants.HotArticleConstants;
import cn.polister.constants.SystemConstants;
import cn.polister.entity.*;
import cn.polister.entity.dto.ArticleDto;
import cn.polister.entity.vo.*;
import cn.polister.mapper.ArticleMapper;
import cn.polister.mapper.ArticleTagMapper;
import cn.polister.mapper.CategoryMapper;
import cn.polister.service.ArticleService;
import cn.polister.service.ArticleTagService;
import cn.polister.utils.BeanCopyUtils;
import cn.polister.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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

    @Resource
    RedisCache redisCache;

    @Resource
    ArticleTagService articleTagService;

    @Resource
    ArticleTagMapper articleTagMapper;

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

        // feature: 从Redis中获取阅读量 2024.1.21
        records.forEach(article -> {
            Integer viewCount = redisCache.getCacheMapValue(
                    SystemConstants.REDIS_VIEW_COUNT_KEY, article.getId().toString());

            article.setViewCount(viewCount.longValue());
        });

        // 获取分类表缓存
        List<Category> list = categoryMapper.selectList(null);
        Map<Long, String> idNameMap = new HashMap<>();
        list.forEach(o -> idNameMap.put(o.getId(), o.getName()));
        // 注入分类名
        records.forEach(o -> o.setCategoryName(idNameMap.get(o.getCategoryId())));
        // vo优化
        List<ArticlePageVo> articlePageVos = BeanCopyUtils.copyBeanList(records, ArticlePageVo.class);

        return ResponseResult.okResult(new ArticleListPageVo(articlePageVos, page.getTotal()));
    }

    @Override
    public ResponseResult getArticleDetails(Long id) {

        Article article = this.getById(id);
        // 拿到正确的浏览量
        Integer cacheMapValue = redisCache.getCacheMapValue(SystemConstants.REDIS_VIEW_COUNT_KEY, id.toString());
        article.setViewCount(cacheMapValue.longValue());
        //article.setViewCount();
        return ResponseResult.okResult(article);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        // 调用redis中指定的值进行递增
        redisCache.increaseMapVue(SystemConstants.REDIS_VIEW_COUNT_KEY, id.toString(), 1);

        // 返回结果
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addArticle(ArticleDto articleDto) {
        // 保存文章信息
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
         this.save(article);

        // 保存Tag信息
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId.longValue())).toList();

        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listArticle(Integer pageNum, Integer pageSize, String title, String summary) {

        // 添加条件
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();

        // 按照标题查找
        wrapper.like(StringUtils.hasText(title), Article::getTitle, title);
        // 按照简要查找
        wrapper.like(StringUtils.hasText(summary), Article::getSummary, summary);

        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);

        return ResponseResult.okResult(new ContentArticleListVo(page.getRecords(), page.getTotal()));

    }

    @Override
    public ResponseResult getArticle(Long id) {
        // 先拿到文章
        Article article = this.getById(id);

        //再拿到标签
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId, id);
        List<ArticleTag> articleTags = articleTagMapper.selectList(wrapper);
        List<Long> tagIds = articleTags.stream().map(ArticleTag::getTagId).toList();
        article.setTags(tagIds);

        return ResponseResult.okResult(article);

    }

    @Override
    public ResponseResult updateArticle(Article article) {
        // 对Tag进行保存操作
        // 首先删除该文章所有tag
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId, article.getId());
        articleTagMapper.delete(wrapper);

        // 然后对新的tagId进行写入
        List<ArticleTag> articleTags = article.getTags().stream()
                .map(articleTag -> new ArticleTag(article.getId(), articleTag)).toList();
        articleTagService.saveBatch(articleTags);

        // 更新文章
        this.updateById(article);

        return ResponseResult.okResult();
    }
}
