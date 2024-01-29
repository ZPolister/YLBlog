package cn.polister.schedule;

import cn.polister.constants.SystemConstants;
import cn.polister.entity.Article;
import cn.polister.service.ArticleService;
import cn.polister.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class UpdateViewCountJob {
    @Resource
    private ArticleService articleService;
    @Resource
    private RedisCache redisCache;

    @Scheduled(cron = "*/5 * * * * ?")
    public void updateViewCount() {
        // 先把redis中数据取下来
        Map<String, Integer> viewCounts = redisCache.getCacheMap(SystemConstants.REDIS_VIEW_COUNT_KEY);
//        List<Article> articles = new ArrayList<>();
//        viewCounts.forEach((k, v) -> articles.add(new Article(Long.valueOf(k), v.longValue())));
        // 将原来的修改者取下来
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Article::getUpdateBy, Article::getId);
        List<Article> articles = articleService.getBaseMapper().selectList(wrapper);
        articles.forEach(article -> article.setViewCount(Long.valueOf(viewCounts.get(article.getId().toString()))));

        // 更新数据
        articleService.updateBatchById(articles);
    }
}
