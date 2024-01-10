package cn.polister.service;

import cn.polister.domain.ResponseResult;
import cn.polister.domain.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;


public interface ArticleService extends IService<Article> {

    ResponseResult getHotArticleList();

    ResponseResult getArticleList(Integer pageNum, Integer pageSize, Long categoryId);
}
