package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.DepartmentRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wqz
 * @date 2022/11/7 9:17
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void saveDepartment(Map<String, String> map) {
        Department department = JSONObject.parseObject(JSONObject.toJSONString(map), Department.class);
        Department mongoDepartment = departmentRepository.findByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());

        if (mongoDepartment == null) {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }else {
            department.setId(mongoDepartment.getId());
            department.setCreateTime(mongoDepartment.getCreateTime());
            department.setUpdateTime(new Date());
            department.setIsDeleted(mongoDepartment.getIsDeleted());
            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> getPageByHoscode(Map<String, String> map) {
        Department department = new Department();
        department.setHoscode(map.get("hoscode"));
        department.setIsDeleted(0);
        Example<Department> example = Example.of(department);
        int pageNum = Integer.parseInt(map.get("page"));
        int pageSize = Integer.parseInt(map.get("limit"));
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Order.asc("createTime")));
        Page<Department> page = departmentRepository.findAll(example, pageable);
        return page;
    }

    @Override
    public void remove(Map<String, String> map) {
        Department department = departmentRepository.findByHoscodeAndDepcode(map.get("hoscode"), map.get("depcode"));
        if (department != null) {
            department.setIsDeleted(1);
            departmentRepository.save(department);
        }
    }

    @Override
    public List<DepartmentVo> getDepartmentList(String hoscode) {
        Department department = new Department();
        department.setHoscode(hoscode);
        department.setIsDeleted(0);
        Example<Department> example = Example.of(department);
        List<Department> all = departmentRepository.findAll(example);

        Map<String, List<Department>> collect = all.stream().collect(Collectors.groupingBy(Department::getBigcode));

        List<DepartmentVo> bigDepartmentList = new ArrayList<>();
        for (Map.Entry<String, List<Department>> entry : collect.entrySet()) {
            DepartmentVo bigDepartment = new DepartmentVo();
            String key = entry.getKey();
            List<Department> value = entry.getValue();
            bigDepartment.setDepcode(key);
            bigDepartment.setDepname(value.get(0).getBigname());
            ArrayList<DepartmentVo> smallDepartmentList = new ArrayList<>();
            for (Department v : value) {
                DepartmentVo smallDepartment = new DepartmentVo();
                smallDepartment.setDepcode(v.getDepcode());
                smallDepartment.setDepname(v.getDepname());
                smallDepartmentList.add(smallDepartment);
            }
            bigDepartment.setChildren(smallDepartmentList);
            bigDepartmentList.add(bigDepartment);
        }
        return bigDepartmentList;
    }

    @Override
    public Department getByHoscodeAndDepcode(String hoscode, String depcode) {
        return departmentRepository.findByHoscodeAndDepcode(hoscode, depcode);
    }
}
