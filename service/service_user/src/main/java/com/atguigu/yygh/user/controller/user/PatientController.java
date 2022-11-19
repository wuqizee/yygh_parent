package com.atguigu.yygh.user.controller.user;


import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.common.utils.JwtHelper;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 就诊人表 前端控制器
 * </p>
 *
 * @author wqz
 * @since 2022-11-14
 */
@RestController
@RequestMapping("/user/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/findList")
    public R findList(@RequestHeader String token) {
        Long userId = JwtHelper.getUserId(token);
        List<Patient> list = patientService.findListByUserId(userId);
        return R.ok().data("list", list);
    }

    @PostMapping("/save")
    public R save(@RequestBody Patient patient, @RequestHeader String token) {
        Long userId = JwtHelper.getUserId(token);
        patient.setUserId(userId);
        patientService.save(patient);
        return R.ok();
    }

    @GetMapping("/getById/{id}")
    public R getById(@PathVariable Long id) {
        Patient patient = patientService.getById(id);
        return R.ok().data("patient", patient);
    }

    @PutMapping("/updateById")
    public R updateById(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return R.ok();
    }

    @DeleteMapping("/removeById/{id}")
    public R removeById(@PathVariable Long id) {
        patientService.removeById(id);
        return R.ok();
    }

    @GetMapping("/inner/getById/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        return patientService.getById(id);
    }

}

