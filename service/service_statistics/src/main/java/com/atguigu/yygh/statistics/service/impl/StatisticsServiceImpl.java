package com.atguigu.yygh.statistics.service.impl;

import com.atguigu.yygh.order.client.OrderFeignClient;
import com.atguigu.yygh.statistics.service.StatisticsService;
import com.atguigu.yygh.vo.order.OrderCountQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/18 20:34
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Override
    public Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo) {
        return orderFeignClient.getCountMap(orderCountQueryVo);
    }

}
