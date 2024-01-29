package cn.polister.handler;

import cn.polister.entity.Article;
import cn.polister.entity.LoginUser;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId;
        try {
            LoginUser loginUser = (LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userId = loginUser.getUser().getId();
        } catch (Exception e) {
            //e.printStackTrace();
            userId = -1L;//表示是自己创建
        }
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createBy", userId, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId;
        try {
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userId = loginUser.getUser().getId();
        } catch (Exception e) {
            Object originalObject = metaObject.getOriginalObject();
            if(originalObject.getClass() == Article.class) {
                userId = ((Article) originalObject).getUpdateBy();
            } else {
                userId = null;//表示是自己创建
            }
        }

        this.setFieldValByName("updateTime", new Date(), metaObject);
        if (userId != null) {
            this.setFieldValByName("updateBy", userId, metaObject);
        }
    }
}