package cn.polister;

import cn.polister.entity.RoleMenu;
import cn.polister.mapper.RoleMenuMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class MapperTest {

    @Resource
    RoleMenuMapper roleMenuMapper;
    //@Test
    void test() {
        List<RoleMenu> list = new ArrayList<>();

        list.add(new RoleMenu(11L, 22L));
        list.add(new RoleMenu(22L, 33L));

        roleMenuMapper.insertList(list);
    }
}
