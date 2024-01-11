package cn.polister.controller;

import cn.polister.entity.ResponseResult;
import cn.polister.service.ArticleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {

    final
    ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/hotArticleList")
    public ResponseResult getHotArticleList() {
        return articleService.getHotArticleList();
    }

    @GetMapping("/articleList")
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize, Long categoryId) {
        return articleService.getArticleList(pageNum, pageSize, categoryId);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleDetails(@PathVariable Long id) {
        return articleService.getArticleDetails(id);
    }

}
