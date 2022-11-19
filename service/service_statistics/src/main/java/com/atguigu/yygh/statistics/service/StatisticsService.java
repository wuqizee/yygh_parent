package com.atguigu.yygh.statistics.service;

import com.atguigu.yygh.vo.order.OrderCountQueryVo;

import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/18 20:34
 */
public interface StatisticsService {

    Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo);

}
