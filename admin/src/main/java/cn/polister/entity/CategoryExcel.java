package cn.polister.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CategoryExcel {
    @ExcelProperty("序号")
    private Long id;
    //分类名
    @ExcelProperty("分类名")
    private String name;
    //描述
    @ExcelProperty("描述")
    private String description;
    //状态0:正常,1禁用
    @ExcelProperty("状态")
    private String status;
}
