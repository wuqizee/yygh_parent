package com.atguigu.yygh.order.service;

import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.vo.order.OrderCountQueryVo;
import com.atguigu.yygh.vo.order.OrderQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author wqz
 * @since 2022-11-15
 */
public interface OrderInfoService extends IService<OrderInfo> {

    Long submitOrder(String scheduleId, Long patientId);

    Page<OrderInfo> getPageList(Integer pageNum, Integer pageSize, OrderQueryVo orderQueryVo);

    OrderInfo getOrders(String orderId);

    OrderInfo updateStatus(String outTradeNo, Integer status);

    boolean cancelOrder(Long orderId);

    void patientTips();

    Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo);

}
