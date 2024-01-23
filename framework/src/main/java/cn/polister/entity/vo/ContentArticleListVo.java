package cn.polister.entity.vo;

import cn.polister.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContentArticleListVo {
    private List<Article> rows;
    private Long total;
}
