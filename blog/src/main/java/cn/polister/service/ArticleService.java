package cn.polister.service;

import cn.polister.entity.ResponseResult;
import cn.polister.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;


public interface ArticleService extends IService<Article> {

    ResponseResult getHotArticleList();

    ResponseResult getArticleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetails(Long id);
}
