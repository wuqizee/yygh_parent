package com.atguigu.yygh.order.service;

import com.atguigu.yygh.model.order.OrderInfo;

import java.io.IOException;
import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/16 20:27
 */
public interface WeiXiService {

    Map<String, Object> createNative(Long orderId) throws Exception;

    Map<String, String> queryPayStatus(Long orderId) throws Exception;

    boolean refund(Long orderId);

}
