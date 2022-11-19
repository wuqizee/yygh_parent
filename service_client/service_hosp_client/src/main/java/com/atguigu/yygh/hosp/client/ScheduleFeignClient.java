package com.atguigu.yygh.hosp.client;

import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author wqz
 * @date 2022/11/15 19:59
 */
@FeignClient(value = "service-hosp", path = "/user/sched")
public interface ScheduleFeignClient {

    @GetMapping("/inner/getById/{id}")
    public ScheduleOrderVo getScheduleOrderVoById(@PathVariable("id") String id);
}
