package cn.polister;


import cn.polister.constants.SystemConstants;
import cn.polister.entity.Article;
import cn.polister.service.ArticleService;
import cn.polister.utils.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestClass {

    @Resource
    RedisCache redisCache;
    @Resource
    ArticleService articleService;

    void testCountJob() {

        // 先把redis中数据取下来
        Map<String, Integer> viewCounts = redisCache.getCacheMap(SystemConstants.REDIS_VIEW_COUNT_KEY);
        List<Article> articles = new ArrayList<>();
        viewCounts.forEach((k, v) -> articles.add(new Article(Long.valueOf(k), v.longValue())));

        // 更新数据
        articleService.updateBatchById(articles);
    }
}
