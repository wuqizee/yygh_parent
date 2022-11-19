package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.yygh.hosp.result.Result;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/7 8:43
 */
@RestController
@RequestMapping("/api/hosp")
public class ApiDepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/saveDepartment")
    public Result saveDepartment(@RequestParam Map<String, String> map) {
        //验证signkey
        departmentService.saveDepartment(map);
        return Result.ok();
    }

    @PostMapping("/department/list")
    public Result page(@RequestParam Map<String, String> map) {
        //验证signkey
        Page<Department> page = departmentService.getPageByHoscode(map);
        return Result.ok(page);
    }

    @PostMapping("/department/remove")
    public Result remove(@RequestParam Map<String, String> map) {
        //验证signkey
        departmentService.remove(map);
        return Result.ok();
    }

}
