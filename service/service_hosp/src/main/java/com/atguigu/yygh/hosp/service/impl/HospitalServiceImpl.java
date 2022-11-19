package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.cmn.client.DictFeignClient;
import com.atguigu.yygh.enums.DictEnum;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.BookingRule;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author wqz
 * @date 2022/11/5 9:53
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public void saveHospital(Map<String, String> map) {
        String jsonString = JSONObject.toJSONString(map);
        Hospital hospital = JSONObject.parseObject(jsonString, Hospital.class);
        Hospital mongoHospital = hospitalRepository.findByHoscode(map.get("hoscode"));

        if (mongoHospital == null) {
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }else {
            hospital.setId(mongoHospital.getId());
            hospital.setStatus(mongoHospital.getStatus());
            hospital.setCreateTime(mongoHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(mongoHospital.getIsDeleted());
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Hospital getHospitalByHoscode(String hoscode) {
        return hospitalRepository.findByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> page(Integer pageNum, Integer pageSize, HospitalQueryVo hospitalQueryVo) {
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        hospital.setIsDeleted(0);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("hosname", ExampleMatcher.GenericPropertyMatchers.contains()).withIgnoreCase(true);
        Example<Hospital> example = Example.of(hospital, exampleMatcher);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Order.asc("createTime")));
        Page<Hospital> page = hospitalRepository.findAll(example, pageable);

        page.getContent().forEach(item->{
            packHospital(item);
        });
        return page;
    }

    @Autowired
    private DictFeignClient dictFeignClient;

    private void packHospital(Hospital item) {
        long provinceValue = Long.parseLong(item.getProvinceCode());
        long cityValue = Long.parseLong(item.getCityCode());
        long districtValue = Long.parseLong(item.getDistrictCode());
        long hostypeValue = Long.parseLong(item.getHostype());

        String provinceName = dictFeignClient.getNameByValue(provinceValue);
        String cityName = dictFeignClient.getNameByValue(cityValue);
        String districtName = dictFeignClient.getNameByValue(districtValue);
        String hostypeName = dictFeignClient.getNameByDictCodeAndValue(DictEnum.HOSTYPE.getDictCode(), hostypeValue);

        Map<String, Object> param = item.getParam();
        param.put("hostypeString", hostypeName);
        param.put("fullAddress", provinceName + cityName + districtName + item.getAddress());
    }

    @Override
    public void updateStatus(String id, Integer status) {
        Optional<Hospital> optional = hospitalRepository.findById(id);
        if (optional.isPresent()) {
            Hospital hospital = optional.get();
            hospital.setStatus(status);
            hospital.setUpdateTime(new Date());
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Hospital getHospitalById(String id) {
        Optional<Hospital> optional = hospitalRepository.findById(id);
        if (optional.isPresent()) {
            Hospital hospital = optional.get();
            packHospital(hospital);
            return hospital;
        }
        return null;
    }

    @Override
    public List<Hospital> getHospitalByHosNameLike(String hosname) {
        List<Hospital> list = hospitalRepository.findByHosnameLike(hosname);
        return list;
    }

    @Override
    public Map<String, Object> show(String hoscode) {
        Map<String, Object> map = new HashMap<>();
        Hospital hospital = getHospitalByHoscode(hoscode);
        packHospital(hospital);
        map.put("hospital", hospital);
        BookingRule bookingRule = hospital.getBookingRule();
        map.put("bookingRule", bookingRule);
        hospital.setBookingRule(null);
        return map;
    }
}
