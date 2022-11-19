package com.atguigu.yygh.user.service;

import com.atguigu.yygh.model.user.Patient;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 就诊人表 服务类
 * </p>
 *
 * @author wqz
 * @since 2022-11-14
 */
public interface PatientService extends IService<Patient> {

    List<Patient> findListByUserId(Long userId);

    Patient getById(Long id);

}
