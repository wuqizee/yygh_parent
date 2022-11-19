package com.atguigu.yygh.hosp.mapper;

import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 医院设置表 Mapper 接口
 * </p>
 *
 * @author wqz
 * @since 2022-10-28
 */
public interface HospitalSetMapper extends BaseMapper<HospitalSet> {

    @Select("select * from hospital_set where id = 1")
    HospitalSet test();

}
