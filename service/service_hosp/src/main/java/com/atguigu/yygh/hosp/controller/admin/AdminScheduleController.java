package com.atguigu.yygh.hosp.controller.admin;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/8 21:26
 */
@RestController
@RequestMapping("/admin/sched")
public class AdminScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/getScheduleRule/{pageNum}/{pageSize}/{hoscode}/{depcode}")
    public R getScheduleRule(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                             @PathVariable String hoscode, @PathVariable String depcode) {
        Map<String, Object> map = scheduleService.getScheduleRule(pageNum, pageSize, hoscode, depcode);
        return R.ok().data(map);
    }

    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public R getScheduleDetail(@PathVariable String hoscode,
                               @PathVariable String depcode, @PathVariable String workDate) {
        List<Schedule> list = scheduleService.getScheduleDetail(hoscode, depcode, workDate);
        return R.ok().data("list", list);
    }

}
