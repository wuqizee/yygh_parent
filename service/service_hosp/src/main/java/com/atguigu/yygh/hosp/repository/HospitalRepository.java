package com.atguigu.yygh.hosp.repository;

import com.atguigu.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author wqz
 * @date 2022/11/5 9:54
 */
public interface HospitalRepository extends MongoRepository<Hospital, String> {

    Hospital findByHoscode(String hoscode);

    List<Hospital> findByHosnameLike(String hosname);

}
