package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/7 11:06
 */
public interface ScheduleService {

    void saveSchedule(Map<String, String> map);

    Page<Schedule> getPageByHoscode(Map<String, String> map);

    void remove(Map<String, String> map);

    Map<String, Object> getScheduleRule(Integer pageNum, Integer pageSize, String hoscode, String depcode);

    List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate);

    Map<String, Object> getBookingScheduleRule(Integer pageNum, Integer pageSize, String hoscode, String depcode);

    Schedule getByScheduleId(String scheduleId);

    ScheduleOrderVo getScheduleOrderVoById(String id);

    void updateAvailableNumber(String scheduleId, Integer availableNumber);

}
