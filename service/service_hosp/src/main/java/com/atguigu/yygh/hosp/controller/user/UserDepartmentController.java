package com.atguigu.yygh.hosp.controller.user;

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
 * @date 2022/11/10 10:05
 */
@RestController
@RequestMapping("/user/dept")
public class UserDepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/getDepartment/{hoscode}")
    public R getDepartment(@PathVariable String hoscode) {
        List<DepartmentVo> list = departmentService.getDepartmentList(hoscode);
        return R.ok().data("list", list);
    }

}
