package com.atguigu.yygh.order.client;

import com.atguigu.yygh.vo.order.OrderCountQueryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/18 20:38
 */
@FeignClient(value = "service-order", path = "/user/order")
public interface OrderFeignClient {

    @PostMapping("/getCountMap")
    public Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo);

}