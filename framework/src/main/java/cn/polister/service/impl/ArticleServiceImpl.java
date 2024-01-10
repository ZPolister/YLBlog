package cn.polister.service.impl;

import cn.polister.constants.ArticleConstants;
import cn.polister.constants.HotArticleConstants;
import cn.polister.domain.ResponseResult;
import cn.polister.domain.entity.Article;
import cn.polister.domain.vo.HotArticle;
import cn.polister.mapper.ArticleMapper;
import cn.polister.service.ArticleService;
import cn.polister.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
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
}
