package cn.polister.runner;

import cn.polister.constants.FrameworkSystemConstants;
import cn.polister.entity.Article;
import cn.polister.mapper.ArticleMapper;
import cn.polister.utils.RedisCache;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Resource
    ArticleMapper articleMapper;
    @Resource
    RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        // 首先拿到文章
        List<Article> articles = articleMapper.selectList(null);
        // 取出文章阅读量
        Map<String, Integer> collect = articles.stream().collect(Collectors.toMap(
                article -> article.getId().toString(),
                article -> article.getViewCount().intValue()));

        // 存到redis中
        redisCache.setCacheMap(FrameworkSystemConstants.REDIS_VIEW_COUNT_KEY, collect);
    }
}
