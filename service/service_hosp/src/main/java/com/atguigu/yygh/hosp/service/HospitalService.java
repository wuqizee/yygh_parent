package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/5 9:53
 */
public interface HospitalService {

    void saveHospital(Map<String, String> map);

    Hospital getHospitalByHoscode(String hoscode);

    Page<Hospital> page(Integer pageNum, Integer pageSize, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Hospital getHospitalById(String id);

    List<Hospital> getHospitalByHosNameLike(String hosname);

    Map<String, Object> show(String hoscode);

}
