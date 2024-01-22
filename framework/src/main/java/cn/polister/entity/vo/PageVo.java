package cn.polister.entity.vo;

import cn.polister.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@Data
public class PageVo {
    private List<Tag> rows;
    private Long total;
    public PageVo(List<Tag> rows, Long total) {
        this.rows = rows;
        this.total = total;
    }
}
