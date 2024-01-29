package cn.polister.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryUpdateVo {
    private String name;
    private String description;
    private String status;
    private Long id;
}
