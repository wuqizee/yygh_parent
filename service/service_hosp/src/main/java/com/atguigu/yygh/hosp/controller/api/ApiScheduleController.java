package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.yygh.hosp.result.Result;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/7 11:06
 */
@RestController
@RequestMapping("/api/hosp")
public class ApiScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/saveSchedule")
    public Result saveSchedule(@RequestParam Map<String, String> map) {
        //验证signkey
        scheduleService.saveSchedule(map);
        return Result.ok();
    }

    @PostMapping("/schedule/list")
    public Result page(@RequestParam Map<String, String> map) {
        //验证signkey
        Page<Schedule> page = scheduleService.getPageByHoscode(map);
        return Result.ok(page);
    }

    @PostMapping("/schedule/remove")
    public Result remove(@RequestParam Map<String, String> map) {
        //验证signkey
        scheduleService.remove(map);
        return Result.ok();
    }


}
