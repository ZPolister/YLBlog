package cn.polister.controller;

import cn.polister.entity.ResponseResult;
import cn.polister.entity.dto.RoleAddDto;
import cn.polister.entity.dto.RoleChangeStatusDto;
import cn.polister.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @GetMapping("/list")
    @PreAuthorize("@permissionService.hasPermission('system:user:query')")
    public ResponseResult listRoleByPage(Integer pageNum, Integer pageSize, String roleName, String status) {
        return roleService.listRoleByPage(pageNum, pageSize, roleName, status);
    }

    @PutMapping("/changeStatus")
    @PreAuthorize("@permissionService.hasPermission('system:user:edit')")
    public ResponseResult changeRoleStatus(@RequestBody RoleChangeStatusDto roleChangeStatusDto) {
        return roleService.changeRoleStatus(roleChangeStatusDto);
    }

    @PostMapping
    @PreAuthorize("@permissionService.hasPermission('system:user:add')")
    public ResponseResult addRole(@RequestBody RoleAddDto roleAddDto) {
        return roleService.addRole(roleAddDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getRoleInfo(@PathVariable Long id) {
        return roleService.getRoleInfo(id);
    }

    @PutMapping()
    @PreAuthorize("@permissionService.hasPermission('system:user:edit')")
    public ResponseResult updateRoleInfo(@RequestBody RoleAddDto roleAddDto) {
        return roleService.updateRoleInfo(roleAddDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@permissionService.hasPermission('system:user:remove')")
    public ResponseResult deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole() {
        return roleService.listAllRole();
    }

}
