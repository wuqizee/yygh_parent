package com.atguigu.yygh.hosp.controller.admin;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @author wqz
 * @date 2022/11/7 16:24
 */
@RestController
@RequestMapping("/admin/hosp")
public class AdminHospitalController {

    @Autowired
    private HospitalService hospitalService;

    @PostMapping("/page/{pageNum}/{pageSize}")
    public R page(@PathVariable Integer pageNum,
                  @PathVariable Integer pageSize,
                  @RequestBody HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> page = hospitalService.page(pageNum, pageSize, hospitalQueryVo);
        return R.ok().data("total", page.getTotalElements()).data("list", page.getContent());
    }

    @PutMapping("/status/{id}/{status}")
    public R updateStatus(@PathVariable String id, @PathVariable Integer status) {
        hospitalService.updateStatus(id, status);
        return R.ok();
    }

    @GetMapping("/get/{id}")
    public R getHospitalById(@PathVariable String id) {
        Hospital hospital = hospitalService.getHospitalById(id);
        return R.ok().data("hospital", hospital);
    }

}
