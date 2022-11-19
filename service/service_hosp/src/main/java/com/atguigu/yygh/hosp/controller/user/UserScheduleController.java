package com.atguigu.yygh.hosp.controller.user;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/14 17:46
 */
@RestController
@RequestMapping("/user/sched")
public class UserScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/getBookingScheduleRule/{pageNum}/{pageSize}/{hoscode}/{depcode}")
    public R getBookingScheduleRule(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                                    @PathVariable String hoscode, @PathVariable String depcode) {
        Map<String, Object> map = scheduleService.getBookingScheduleRule(pageNum, pageSize, hoscode, depcode);
        return R.ok().data(map);
    }

    @GetMapping("/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public R findScheduleList(@PathVariable String hoscode, @PathVariable String depcode,
                              @PathVariable String workDate) {
        List<Schedule> list = scheduleService.getScheduleDetail(hoscode, depcode, workDate);
        return R.ok().data("list", list);
    }

    @GetMapping("/getSchedule/{id}")
    public R getSchedule(@PathVariable String id) {
        Schedule schedule = scheduleService.getByScheduleId(id);
        return R.ok().data("schedule", schedule);
    }

    @GetMapping("/inner/getById/{id}")
    public ScheduleOrderVo getScheduleOrderVoById(@PathVariable("id") String id) {
        ScheduleOrderVo schedule = scheduleService.getScheduleOrderVoById(id);
        return schedule;
    }
}
