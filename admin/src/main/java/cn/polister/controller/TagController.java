package cn.polister.controller;

import cn.polister.entity.ResponseResult;
import cn.polister.entity.Tag;
import cn.polister.service.TagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Resource
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult listByPage(Integer pageNum, Integer pageSize, String name, String remark) {
        return tagService.listByPage(pageNum, pageSize, name, remark);
    }

    @PostMapping("")
    public ResponseResult addTag(@RequestBody Tag tag) {
        return tagService.addTag(tag);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable Long id) {
        return tagService.deleteTag(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable Long id) {
        return tagService.getTag(id);
    }

    @PutMapping()
    public ResponseResult updateTag(@RequestBody Tag tag) {
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag() {
        return ResponseResult.okResult(tagService.list());
    }
}
