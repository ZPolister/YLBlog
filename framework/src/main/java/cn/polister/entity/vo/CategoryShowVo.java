package cn.polister.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryShowVo {

    private Long id;
    //分类名
    private String name;
    //简介
    private String description;
}
