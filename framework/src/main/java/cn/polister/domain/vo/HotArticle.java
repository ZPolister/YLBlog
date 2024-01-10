package cn.polister.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 热门文章Vo返回类
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HotArticle {
    // 文章id
    private Long id;
    // 标题
    private String title;
    //访问量
    private Long viewCount;
}
