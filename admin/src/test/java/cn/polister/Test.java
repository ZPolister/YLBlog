package cn.polister;

import cn.polister.mapper.RoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class Test {
    @Autowired
    RoleMenuMapper roleMenuMapper;
    //@org.junit.jupiter.api.Test
    void test() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);

        roleMenuMapper.findMenuByIds(ids);
    }
}
