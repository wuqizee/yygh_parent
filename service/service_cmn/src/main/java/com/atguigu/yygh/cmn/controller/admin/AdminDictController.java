package com.atguigu.yygh.cmn.controller.admin;

import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 组织架构表 前端控制器
 * </p>
 *
 * @author wqz
 * @since 2022-11-02
 */
@RestController
@RequestMapping("/admin/cmn")
public class AdminDictController {

    @Autowired
    private DictService dictService;

    @GetMapping("/getByParentId/{parentId}")
    public R getDictListByParentId(@PathVariable Long parentId) {
        return R.ok().data("list", dictService.getDictListByParentId(parentId));
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        dictService.download(response);
    }

    @PostMapping("/upload")
    public R upload(MultipartFile excelFile) throws IOException {
        dictService.upload(excelFile);
        return R.ok();
    }

    @GetMapping("/name/{value}")
    public String getNameByValue(@PathVariable("value") Long value) {
        return dictService.getNameByValue(value);
    }

    @GetMapping("/name/{dictCode}/{value}")
    public String getNameByDictCodeAndValue(@PathVariable("dictCode") String dictCode, @PathVariable("value") Long value) {
        return dictService.getNameByDictCodeAndValue(dictCode, value);
    }

    @GetMapping("/getByDictCode/{dictCode}")
    public R getDictListByDictCode(@PathVariable String dictCode) {
        List<Dict> list = dictService.getDictListByDictCode(dictCode);
        return R.ok().data("list", list);
    }




}

