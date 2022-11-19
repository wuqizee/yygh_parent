package com.atguigu.yygh.hosp.controller.admin;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wqz
 * @date 2022/11/8 21:00
 */
@RestController
@RequestMapping("/admin/dept")
public class AdminDepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/getDepartmentList/{hoscode}")
    public R getDepartmentList(@PathVariable String hoscode) {
        List<DepartmentVo> list = departmentService.getDepartmentList(hoscode);
        return R.ok().data("list", list);
    }

}
