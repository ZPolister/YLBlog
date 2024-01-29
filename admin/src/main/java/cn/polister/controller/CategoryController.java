package cn.polister.controller;

import cn.polister.entity.Category;
import cn.polister.entity.CategoryExcel;
import cn.polister.entity.ResponseResult;
import cn.polister.entity.dto.CategoryDto;
import cn.polister.entity.dto.CategoryUpdateVo;
import cn.polister.enums.AppHttpCodeEnum;
import cn.polister.service.CategoryService;
import cn.polister.utils.BeanCopyUtils;
import cn.polister.utils.WebUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory() {
        return categoryService.getCategoryList();
    }

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, String name, String status) {
        return categoryService.list(pageNum, pageSize, name, status);
    }

    @GetMapping("/{id}")
    public ResponseResult getCategoryInfo(@PathVariable Long id) {
        return categoryService.getCategoryInfo(id);
    }

    @PostMapping()
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @PutMapping()
    public ResponseResult updateCategory(@RequestBody CategoryUpdateVo categoryUpdateVo) {
        return categoryService.updateCategory(categoryUpdateVo);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/export")
    public void downloadFailedUsingJson(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            // 导出分类
            List<Category> list = categoryService.list();
            List<CategoryExcel> categoryExcels = BeanCopyUtils.copyBeanList(list, CategoryExcel.class);
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), CategoryExcel.class)
                    .autoCloseStream(Boolean.FALSE).sheet("分类数据")
                    .doWrite(categoryExcels);
        } catch (Exception e) {
            // 重置response
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(responseResult));
        }
    }
}
