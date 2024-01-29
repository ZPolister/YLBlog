package cn.polister.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryPageVo {
    private List<CategoryListVo> rows;
    private Long total;
}
