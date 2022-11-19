package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YYGHException;
import com.atguigu.yygh.hosp.repository.ScheduleRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.BookingRule;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.BookingScheduleRuleVo;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wqz
 * @date 2022/11/7 11:06
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public void saveSchedule(Map<String, String> map) {
        Schedule schedule = JSONObject.parseObject(JSONObject.toJSONString(map), Schedule.class);
        Schedule mongoSchedule = scheduleRepository.findByHoscodeAndDepcodeAndHosScheduleId(map.get("hoscode"), map.get("depcode"), map.get("hosScheduleId"));
        if (mongoSchedule == null) {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            scheduleRepository.save(schedule);
        }else {
            schedule.setId(mongoSchedule.getId());
            schedule.setCreateTime(mongoSchedule.getCreateTime());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(mongoSchedule.getIsDeleted());
            scheduleRepository.save(schedule);
        }
    }

    @Override
    public Page<Schedule> getPageByHoscode(Map<String, String> map) {
        Schedule schedule = new Schedule();
        schedule.setHoscode(map.get("hoscode"));
        schedule.setIsDeleted(0);
        Example<Schedule> example = Example.of(schedule);
        int pageNum = Integer.parseInt(map.get("page"));
        int pageSize = Integer.parseInt(map.get("limit"));
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Order.asc("createTime")));
        Page<Schedule> page = scheduleRepository.findAll(example, pageable);
        return page;
    }

    @Override
    public void remove(Map<String, String> map) {
        Schedule schedule = scheduleRepository.findByHoscodeAndHosScheduleId(map.get("hoscode"), map.get("hosScheduleId"));
        if (schedule != null) {
            schedule.setIsDeleted(1);
            scheduleRepository.save(schedule);
        }
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Map<String, Object> getScheduleRule(Integer pageNum, Integer pageSize, String hoscode, String depcode) {
        Map<String, Object> map = new HashMap<>();
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
                    .first("workDate").as("workDate")
                    .sum("reservedNumber").as("reservedNumber")
                    .sum("availableNumber").as("availableNumber"),
                Aggregation.sort(Sort.by(Sort.Order.asc("workDate"))),
                Aggregation.skip((long)(pageNum - 1) * pageSize),
                Aggregation.limit(pageSize)
        );
        AggregationResults<BookingScheduleRuleVo> aggregate =
                mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> mappedResults = aggregate.getMappedResults();
        mappedResults.forEach(item->{
            Date workDate = item.getWorkDate();
            String dayOfWeek = getDayOfWeek(new DateTime(workDate));
            item.setDayOfWeek(dayOfWeek);
            item.setWorkDate(workDate);
        });
        map.put("list", mappedResults);

        Criteria criteria2 = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        Aggregation aggregation2 = Aggregation.newAggregation(
                Aggregation.match(criteria2),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> aggregate2 =
                mongoTemplate.aggregate(aggregation2, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> mappedResults2 = aggregate2.getMappedResults();
        map.put("total", mappedResults2.size());

        Hospital hospital = hospitalService.getHospitalByHoscode(hoscode);
        Map<String, Object> baseMap = new HashMap<>();
        baseMap.put("hosname", hospital.getHosname());
        map.put("baseMap", baseMap);
        return map;
    }

    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
                break;
        }
        return dayOfWeek;
    }

    @Override
    public List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate) {
        Date date = DateTime.parse(workDate).toDate();
        List<Schedule> scheduleList = scheduleRepository.findByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, date);
        if (scheduleList == null) {
            return null;
        }
        return scheduleList;
    }

    @Override
    public Map<String, Object> getBookingScheduleRule(Integer pageNum, Integer pageSize, String hoscode, String depcode) {
        Hospital hospital = hospitalService.getHospitalByHoscode(hoscode);
        BookingRule bookingRule = hospital.getBookingRule();
        IPage<Date> iPage = getDatePage(pageNum, pageSize, bookingRule);
        List<Date> records = iPage.getRecords();

        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode).and("workDate").in(records);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
                    .first("workDate").as("workDate")
                    .count().as("docCount")
                    .sum("reservedNumber").as("reservedNumber")
                    .sum("availableNumber").as("availableNumber")
        );
        AggregationResults<BookingScheduleRuleVo> aggregate =
                mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> mappedResults = aggregate.getMappedResults();
        Map<Date, BookingScheduleRuleVo> collect = mappedResults.stream()
                .collect(Collectors.toMap(BookingScheduleRuleVo::getWorkDate, BookingScheduleRuleVo -> BookingScheduleRuleVo));

        List<BookingScheduleRuleVo> list = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            Date date = records.get(i);
            BookingScheduleRuleVo bookingScheduleRuleVo = collect.get(date);
            if (bookingScheduleRuleVo == null) {
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                bookingScheduleRuleVo.setAvailableNumber(-1);
            }
            bookingScheduleRuleVo.setWorkDate(date);
            bookingScheduleRuleVo.setDayOfWeek(getDayOfWeek(new DateTime(date)));
            bookingScheduleRuleVo.setStatus(0);

            if (pageNum == 1 && i == 0) {
                DateTime stopTime = getDateTime(new Date(), bookingRule.getStopTime());
                if (stopTime.isBeforeNow()) {
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            if (pageNum == iPage.getPages() && i == records.size() - 1) {
                bookingScheduleRuleVo.setStatus(1);
            }
            list.add(bookingScheduleRuleVo);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", iPage.getTotal());
        Map<String, Object> baseMap = new HashMap<>();
        baseMap.put("hosname", hospital.getHosname());
        Department department = departmentService.getByHoscodeAndDepcode(hoscode, depcode);
        baseMap.put("bigname", department.getBigname());
        baseMap.put("depname", department.getDepname());
        baseMap.put("workDateString", new DateTime().toString("yyyy年MM月"));
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
        map.put("baseMap", baseMap);
        return map;
    }

    private IPage<Date> getDatePage(Integer pageNum, Integer pageSize, BookingRule bookingRule) {
        Integer cycle = bookingRule.getCycle();
        DateTime releaseTime = getDateTime(new Date(), bookingRule.getReleaseTime());
        if (releaseTime.isBeforeNow()) {
            cycle++;
        }

        int start = (pageNum - 1) * pageSize;
        int end = start + pageSize;
        if (end > cycle) {
            end = cycle;
        }
        List<Date> dateList = new ArrayList<>();
        for (int i = start; i < end; i++) {
            //yyyy-MM-dd的时间格式去查
            String dateString = new DateTime().plusDays(i).toString("yyyy-MM-dd");
            Date date = new DateTime(dateString).toDate();
            dateList.add(date);
        }

        IPage<Date> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize, cycle);
        iPage.setRecords(dateList);
        return iPage;
    }

    private DateTime getDateTime(Date date, String s) {
        String dateString = new DateTime(date).toString("yyyy-MM-dd") + " " + s;
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateString);
    }

    @Override
    public Schedule getByScheduleId(String id) {
        Optional<Schedule> optional = scheduleRepository.findById(id);
        if (!optional.isPresent()) {
            throw new YYGHException(20001, "排版不存在");
        }
        Schedule schedule = optional.get();
        String hoscode = schedule.getHoscode();
        String depcode = schedule.getDepcode();
        schedule.getParam().put("dayOfWeek", getDayOfWeek(new DateTime(schedule.getWorkDate())));
        schedule.getParam().put("hosname", hospitalService.getHospitalByHoscode(hoscode).getHosname());
        schedule.getParam().put("depname", departmentService.getByHoscodeAndDepcode(hoscode, depcode).getDepname());
        return schedule;
    }

    @Override
    public ScheduleOrderVo getScheduleOrderVoById(String id) {
        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        Schedule schedule = scheduleRepository.findById(id).get();
        BeanUtils.copyProperties(schedule, scheduleOrderVo);
        Hospital hospital = hospitalService.getHospitalByHoscode(schedule.getHoscode());
        BookingRule bookingRule = hospital.getBookingRule();
        Department department = departmentService.getByHoscodeAndDepcode(schedule.getHoscode(), schedule.getDepcode());

        scheduleOrderVo.setHosname(hospital.getHosname());
        scheduleOrderVo.setDepname(department.getDepname());
        scheduleOrderVo.setReserveDate(schedule.getWorkDate());
        scheduleOrderVo.setReserveTime(schedule.getWorkTime());

        Date quitTime = getDateTime(new DateTime(schedule.getWorkDate())
                .plusDays(bookingRule.getQuitDay()).toDate(), bookingRule.getQuitTime()).toDate();
        scheduleOrderVo.setQuitTime(quitTime);

        DateTime startTime = getDateTime(new Date(), bookingRule.getReleaseTime());
        scheduleOrderVo.setStartTime(startTime.toDate());

        DateTime endTime = getDateTime(new DateTime().plusDays(bookingRule.getCycle()).toDate(), bookingRule.getStopTime());
        scheduleOrderVo.setEndTime(endTime.toDate());

        DateTime stopTime = getDateTime(schedule.getWorkDate(), bookingRule.getStopTime());
        scheduleOrderVo.setStopTime(stopTime.toDate());

        return scheduleOrderVo;
    }

    @Override
    public void updateAvailableNumber(String scheduleId, Integer availableNumber) {
        Optional<Schedule> optional = scheduleRepository.findById(scheduleId);
        if (optional.isPresent()) {
            Schedule schedule = optional.get();
            schedule.setAvailableNumber(availableNumber);
            scheduleRepository.save(schedule);
        }
    }


}
