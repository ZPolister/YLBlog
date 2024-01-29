package cn.polister.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListVo {
    private String description;
    private Long id;
    private String name;
    private String status;
}
