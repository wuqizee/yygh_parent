package com.atguigu.yygh.hosp.repository;

import com.atguigu.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author wqz
 * @date 2022/11/7 9:17
 */
public interface DepartmentRepository extends MongoRepository<Department, String> {

    Department findByHoscodeAndDepcode(String hoscode, String depcode);

}
