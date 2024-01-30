package cn.polister.controller;

import cn.polister.entity.Menu;
import cn.polister.entity.ResponseResult;
import cn.polister.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Resource
    private MenuService menuService;

    @GetMapping("/list")
    @PreAuthorize("@permissionService.hasPermission('system:menu:query')")
    public ResponseResult listAllMenus(String status, String menuName) {
        return menuService.listAllMenus(status, menuName);
    }

    @PostMapping()
    @PreAuthorize("@permissionService.hasPermission('system:menu:add')")
    public ResponseResult addMenu(@RequestBody Menu menu) {
        return menuService.addMenu(menu);
    }

    @GetMapping("/{id}")
    public ResponseResult getMenuInfo(@PathVariable Long id) {
        return menuService.getMenuInfo(id);
    }

    @PutMapping()
    @PreAuthorize("@permissionService.hasPermission('system:menu:edit')")
    public ResponseResult updateMenuInfo(@RequestBody Menu menu) {
        return menuService.updateMenuInfo(menu);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@permissionService.hasPermission('system:menu:remove')")
    public ResponseResult deleteMenu(@PathVariable Long id) {
        return menuService.deleteMenu(id);
    }

    @GetMapping("/treeselect")
    public ResponseResult getTreeSelect() {
        return ResponseResult.okResult(menuService.getTreeSelect());
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult getTreeSelectByRole(@PathVariable Long id) {
        return menuService.getTreeSelectByRole(id);
    }

}
