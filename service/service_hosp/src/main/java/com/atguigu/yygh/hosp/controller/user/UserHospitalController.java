package com.atguigu.yygh.hosp.controller.user;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/9 20:15
 */
@RestController
@RequestMapping("/user/hosp")
public class UserHospitalController {

    @Autowired
    private HospitalService hospitalService;

    @GetMapping("/page/{pageNum}/{pageSize}")
    public R page(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                  HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> page = hospitalService.page(pageNum, pageSize, hospitalQueryVo);
        return R.ok().data("pages", page);
    }

    @GetMapping("/getByHosname/{hosname}")
    public R getByHosname(@PathVariable String hosname) {
        List<Hospital> hospitalList = hospitalService.getHospitalByHosNameLike(hosname);
        return R.ok().data("list", hospitalList);
    }

    @GetMapping("/show/{hoscode}")
    public R show(@PathVariable String hoscode) {
        Map<String, Object> map = hospitalService.show(hoscode);
        return R.ok().data(map);
    }

}
