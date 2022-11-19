package com.atguigu.yygh.cmn.controller.user;

import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.model.cmn.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wqz
 * @date 2022/11/9 20:51
 */
@RestController
@RequestMapping("/user/cmn")
public class UserDictController {

    @Autowired
    private DictService dictService;

    @GetMapping("/getByDictCode/{dictCode}")
    public R getDictListByDictCode(@PathVariable String dictCode) {
        List<Dict> list = dictService.getDictListByDictCode(dictCode);
        return R.ok().data("list", list);
    }

    @GetMapping("/findByParentId/{parentId}")
    public R findByParentId(@PathVariable Long parentId) {
        List<Dict> list = dictService.getDictListByParentId(parentId);
        return R.ok().data("list", list);
    }

}
