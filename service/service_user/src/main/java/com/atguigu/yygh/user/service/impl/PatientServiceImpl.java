package com.atguigu.yygh.user.service.impl;

import com.atguigu.yygh.cmn.client.DictFeignClient;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.user.mapper.PatientMapper;
import com.atguigu.yygh.user.service.PatientService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 就诊人表 服务实现类
 * </p>
 *
 * @author wqz
 * @since 2022-11-14
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private PatientMapper patientMapper;

    @Override
    public List<Patient> findListByUserId(Long userId) {
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Patient> list = patientMapper.selectList(queryWrapper);
        list.stream().forEach(item->{
            packPatient(item);
        });
        return list;
    }

    @Autowired
    private DictFeignClient dictFeignClient;

    private void packPatient(Patient item) {
        //封装证件类型
        long certificatesTypeValue = Long.parseLong(item.getCertificatesType());
        String certificatesTypeName = dictFeignClient.getNameByValue(certificatesTypeValue);
        item.getParam().put("certificatesTypeString", certificatesTypeName);

        //封装省市区
        long provinceCodeValue = Long.parseLong(item.getProvinceCode());
        long CityCodeValue = Long.parseLong(item.getCityCode());
        long districtCodeValue = Long.parseLong(item.getDistrictCode());
        String provinceCodeName = dictFeignClient.getNameByValue(provinceCodeValue);
        String cityCodeName = dictFeignClient.getNameByValue(CityCodeValue);
        String districtCodeName = dictFeignClient.getNameByValue(districtCodeValue);
        item.getParam().put("provinceString", provinceCodeName);
        item.getParam().put("cityString", cityCodeName);
        item.getParam().put("districtString", districtCodeName);

        StringBuilder fullAddress = new StringBuilder();
        fullAddress.append(provinceCodeName).append(cityCodeName).append(districtCodeName).append(item.getAddress());
        item.getParam().put("fullAddress", fullAddress.toString());
    }

    @Override
    public Patient getById(Long id) {
        Patient patient = patientMapper.selectById(id);
        packPatient(patient);
        return patient;
    }


}
