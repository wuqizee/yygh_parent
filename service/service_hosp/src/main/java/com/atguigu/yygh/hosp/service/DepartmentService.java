package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/7 9:17
 */
public interface DepartmentService {

    void saveDepartment(Map<String, String> map);

    Page<Department> getPageByHoscode(Map<String, String> map);

    void remove(Map<String, String> map);

    List<DepartmentVo> getDepartmentList(String hoscode);

    Department getByHoscodeAndDepcode(String hoscode, String depcode);

}
