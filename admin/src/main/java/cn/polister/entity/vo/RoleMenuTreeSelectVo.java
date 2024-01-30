package cn.polister.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleMenuTreeSelectVo {
    private List<MenuTreeVo> menus;
    private List<Long> checkedKeys;
}
