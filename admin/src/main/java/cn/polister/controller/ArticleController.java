package cn.polister.controller;

import cn.polister.entity.Article;
import cn.polister.entity.ResponseResult;
import cn.polister.entity.dto.ArticleDto;
import cn.polister.service.ArticleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/article")
@PreAuthorize("@permissionService.hasPermission('content:article:list')")
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @PreAuthorize("@permissionService.hasPermission('content:article:writer')")
    @PostMapping()
    public ResponseResult addArticle(@RequestBody ArticleDto articleDto) {
        return articleService.addArticle(articleDto);
    }

    @GetMapping("/list")
    public ResponseResult listArticle(Integer pageNum, Integer pageSize, String title, String summary) {
        return articleService.listArticle(pageNum, pageSize, title, summary);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticle(@PathVariable Long id) {
        return articleService.getArticle(id);
    }

    @PutMapping()
    public ResponseResult updateArticle(@RequestBody Article article) {
        return articleService.updateArticle(article);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable Long id) {
        articleService.removeById(id);
        return ResponseResult.okResult();
    }
}
