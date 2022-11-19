package com.atguigu.yygh.statistics.controller;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.statistics.service.StatisticsService;
import com.atguigu.yygh.vo.order.OrderCountQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/18 20:33
 */
@RestController
@RequestMapping("/admin/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @PostMapping("/getCountMap")
    public R getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo) {
        Map<String, Object> map = statisticsService.getCountMap(orderCountQueryVo);
        return R.ok().data(map);
    }

}
