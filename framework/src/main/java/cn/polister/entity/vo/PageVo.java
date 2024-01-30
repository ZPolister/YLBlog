package cn.polister.entity.vo;

import cn.polister.entity.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class PageVo {
    private List rows;
    private Long total;

    public PageVo(List rows, Long total) {
        this.rows = rows;
        this.total = total;
    }
}
