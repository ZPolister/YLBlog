package cn.polister.service;

import cn.polister.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2024-01-22 15:38:37
 */
public interface RoleService extends IService<Role> {

    List<Role> getRolesByUserId(Long id);
}
