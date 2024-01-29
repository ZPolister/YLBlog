package cn.polister.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class MenuTreeVo {
    private List<MenuTreeVo> children;
    private Long id;
    private String label;
    private Long parentId;
}
