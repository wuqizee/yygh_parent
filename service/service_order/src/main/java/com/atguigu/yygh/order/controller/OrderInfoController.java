package com.atguigu.yygh.order.controller;


import com.atguigu.yygh.common.exception.YYGHException;
import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.common.utils.JwtHelper;
import com.atguigu.yygh.enums.OrderStatusEnum;
import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.order.service.OrderInfoService;
import com.atguigu.yygh.vo.order.OrderCountQueryVo;
import com.atguigu.yygh.vo.order.OrderQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author wqz
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/user/order")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    @PostMapping("/submitOrder/{scheduleId}/{patientId}")
    public R submitOrder(@PathVariable String scheduleId, @PathVariable Long patientId) {
        Long orderId = orderInfoService.submitOrder(scheduleId, patientId);
        return R.ok().data("orderId", orderId);
    }

    @PostMapping("/getPageList/{pageNum}/{pageSize}")
    public R getPageList(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                         @RequestBody OrderQueryVo orderQueryVo, @RequestHeader String token) {
        orderQueryVo.setUserId(JwtHelper.getUserId(token));
        Page<OrderInfo> page = orderInfoService.getPageList(pageNum, pageSize, orderQueryVo);
        return R.ok().data("page", page);
    }

    @GetMapping("/getStatusList")
    public R getStatusList() {
        return R.ok().data("statusList", OrderStatusEnum.getStatusList());
    }

    @GetMapping("/getOrders/{orderId}")
    public R getOrders(@PathVariable String orderId) {
        OrderInfo orderInfo = orderInfoService.getOrders(orderId);
        return R.ok().data("orderInfo", orderInfo);
    }

    @GetMapping("/cancelOrder/{orderId}")
    public R cancelOrder(@PathVariable Long orderId) {
        boolean flag = orderInfoService.cancelOrder(orderId);
        if (!flag) {
            throw new YYGHException(20001, "退款失败");
        }
        return R.ok();
    }

    @PostMapping("/getCountMap")
    public Map<String, Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo) {
        return orderInfoService.getCountMap(orderCountQueryVo);
    }

}

