package cn.polister.controller;

import cn.polister.entity.Link;
import cn.polister.entity.ResponseResult;
import cn.polister.service.LinkService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController
@RequestMapping("/content/link")
@PreAuthorize("@permissionService.hasPermission('content:link:list')")
public class LinkController {
    @Resource
    private LinkService linkService;

    @GetMapping("/list")
    @PreAuthorize("@permissionService.hasPermission('content:link:query')")
    public ResponseResult listLinkByPage(Integer pageNum, Integer pageSize, String name, String status) {
        return linkService.listLinkByPage(pageNum, pageSize, name, status);
    }

    @PostMapping()
    @PreAuthorize("@permissionService.hasPermission('content:link:add')")
    public ResponseResult addLink(@RequestBody Link link) {
        return linkService.addLink(link);
    }

    @GetMapping("/{id}")
    public ResponseResult getLinkInfo(@PathVariable Long id) {
        return linkService.getLinkInfo(id);
    }

    @PutMapping()
    @PreAuthorize("@permissionService.hasPermission('content:link:edit')")
    public ResponseResult updateLink(@RequestBody Link link) {
        return linkService.updateLink(link);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@permissionService.hasPermission('content:link:remove')")
    public ResponseResult deleteLink(@PathVariable Long id) {
        return linkService.deleteLink(id);
    }

    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody HashMap<String, String> map) {
        return linkService.changeLinkStatus(Long.valueOf(map.get("id")), map.get("status"));
    }
}
