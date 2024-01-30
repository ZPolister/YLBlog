package cn.polister.entity.vo;

import cn.polister.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfoWithRolesVo {
    private List<Long> roleIds;
    private List<Role> roles;
    private UserInfoVo user;
}
