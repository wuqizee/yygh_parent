package com.atguigu.yygh.order.service;

import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.model.order.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/16 20:42
 */
public interface PaymentInfoService extends IService<PaymentInfo> {

    void savePaymentInfo(OrderInfo orderInfo);

    void paySuccess(Map<String, String> resultMap);

    PaymentInfo getByOrderId(Long orderId);

}
