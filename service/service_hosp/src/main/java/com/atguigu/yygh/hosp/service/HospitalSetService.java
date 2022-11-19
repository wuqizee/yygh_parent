package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 医院设置表 服务类
 * </p>
 *
 * @author wqz
 * @since 2022-10-28
 */
public interface HospitalSetService extends IService<HospitalSet> {

    HospitalSet getByHoscode(String hoscode);

}
