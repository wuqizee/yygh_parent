package com.atguigu.yygh.order.controller;

import com.atguigu.yygh.common.exception.YYGHException;
import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.order.service.PaymentInfoService;
import com.atguigu.yygh.order.service.WeiXiService;
import com.github.wxpay.sdk.WXPayConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/16 20:27
 */
@RestController
@RequestMapping("/order/weixi")
public class WeiXiController {

    @Autowired
    private WeiXiService winXiService;

    @Autowired
    private PaymentInfoService paymentInfoService;

    @GetMapping("/createNative/{orderId}")
    public R createNative(@PathVariable Long orderId) throws Exception {
        Map<String, Object> map = winXiService.createNative(orderId);
        return R.ok().data(map);
    }

    @GetMapping("/queryPayStatus/{orderId}")
    public R queryPayStatus(@PathVariable Long orderId) throws Exception {
        Map<String, String> resultMap = winXiService.queryPayStatus(orderId);
        if (!CollectionUtils.isEmpty(resultMap) && WXPayConstants.SUCCESS.equals(resultMap.get("trade_state"))) {
            paymentInfoService.paySuccess(resultMap);
            return R.ok().message("支付成功");
        }
        return R.ok().message("支付中");
    }

}
