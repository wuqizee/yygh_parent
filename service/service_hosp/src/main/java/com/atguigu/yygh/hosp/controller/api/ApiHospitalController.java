package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.yygh.common.exception.YYGHException;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.result.Result;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.hosp.utils.HttpRequestHelper;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/5 8:45
 */
@RestController
@RequestMapping("/api/hosp")
public class ApiHospitalController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request) throws YYGHException {
        Map<String, String[]> paramMap = request.getParameterMap();
        Map<String, String> map = HttpRequestHelper.switchMap(paramMap);
        //验证signkey
        if (StringUtils.isEmpty(map.get("sign"))) {
            throw new YYGHException(20001, "未携带signkey");
        }

        HospitalSet hospitalSet = hospitalSetService.getByHoscode(map.get("hoscode"));
        if (hospitalSet == null) {
            throw new YYGHException(20002, "医院不合格");
        }

        String encrypt = MD5.encrypt(hospitalSet.getSignKey());
        if (!encrypt.equals(map.get("sign"))) {
            throw new YYGHException(20003, "singkey错误");
        }

        String logoData = map.get("logoData").replace(" ", "+");
        map.put("logoData", logoData);
        hospitalService.saveHospital(map);
        return Result.ok();
    }

    @PostMapping("/hospital/show")
    public Result show(@RequestParam Map<String, String> map) {
        //验证signkey
        Hospital hospital = hospitalService.getHospitalByHoscode(map.get("hoscode"));
        return Result.ok(hospital);
    }



}
