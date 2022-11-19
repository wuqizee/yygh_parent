package com.atguigu.yygh.hosp.controller.admin;


import com.atguigu.yygh.common.exception.YYGHException;
import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * <p>
 * 医院设置表 前端控制器
 * </p>
 *
 * @author wqz
 * @since 2022-10-28
 */
@Api(tags = "医院设置接口")
@RestController
@RequestMapping("/admin/hospset")
public class AdminHospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    @PostMapping("/page/{pageNum}/{pageSize}")
    public R page(@PathVariable Integer pageNum,
                  @PathVariable Integer pageSize,
                  @RequestBody HospitalSetQueryVo hospitalSetQueryVo) {
        Page<HospitalSet> page = new Page<>(pageNum, pageSize);
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        String hoscode = hospitalSetQueryVo.getHoscode();
        String hosname = hospitalSetQueryVo.getHosname();
        queryWrapper.like(StringUtils.isNotBlank(hoscode), "hosname", hosname);
        queryWrapper.eq(StringUtils.isNotBlank(hosname), "hoscode", hoscode);
        hospitalSetService.page(page, queryWrapper);
        return R.ok().data("total", page.getTotal()).data("list", page.getRecords());
    }

    @PostMapping("/save")
    public R save(@RequestBody HospitalSet hospitalSet) {
        String signKey = "" + System.currentTimeMillis() + new Random().nextInt(1000);
        //hospitalSet.setSignKey(MD5.encrypt(signKey));
        hospitalSet.setSignKey(signKey);
        hospitalSetService.save(hospitalSet);
        return R.ok();
    }

    @ApiOperation(value = "医院设置删除")
    @ApiImplicitParam(name = "id", value = "医院设置id", required = true, paramType = "path", dataType = "String")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable Long id) {
        hospitalSetService.removeById(id);
        return R.ok();
    }

    @DeleteMapping("/batchRemove")
    public R batchRemove(@RequestBody List<Long> ids) {
        hospitalSetService.removeByIds(ids);
        return R.ok();
    }

    @GetMapping("/get/{id}")
    public R getById(@PathVariable String id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return R.ok().data("item", hospitalSet);
    }

    @PutMapping("/update")
    public R update(@RequestBody HospitalSet hospitalSet) {
        hospitalSetService.updateById(hospitalSet);
        return R.ok();
    }

    @PutMapping("/status/{id}/{status}")
    public R status(@PathVariable("id") Long id, @PathVariable Integer status) {
        HospitalSet hospitalSet = new HospitalSet();
        hospitalSet.setId(id);
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return R.ok();
    }

    @ApiOperation(value = "医院设置列表")
    @GetMapping("/getAll")
    public R getAll() throws YYGHException {
        /*try {
            int i = 1 / 0;
        } catch (Exception e) {
            throw new YYGHException(20001, "预约挂号异常");
        }*/
        return R.ok().data("list", hospitalSetService.list());
    }

}

