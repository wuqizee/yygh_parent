package com.atguigu.yygh.order.service.impl;

import com.atguigu.yygh.common.exception.YYGHException;
import com.atguigu.yygh.enums.RefundStatusEnum;
import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.model.order.PaymentInfo;
import com.atguigu.yygh.model.order.RefundInfo;
import com.atguigu.yygh.order.service.OrderInfoService;
import com.atguigu.yygh.order.service.PaymentInfoService;
import com.atguigu.yygh.order.service.RefundInfoService;
import com.atguigu.yygh.order.service.WeiXiService;
import com.atguigu.yygh.order.utils.ConstantPropertiesUtils;
import com.atguigu.yygh.order.utils.HttpClient;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/16 20:27
 */
@Service
public class WeiXiServiceImpl implements WeiXiService {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Override
    public Map<String, Object> createNative(Long orderId) throws Exception {
        //保存支付交易
        OrderInfo orderInfo = orderInfoService.getById(orderId);
        paymentInfoService.savePaymentInfo(orderInfo);

        //微信支付api
        Map<String, String> paramMap = getParamMap(orderInfo.getOutTradeNo());
        paramMap.put("body", "尚医通-预约支付");
        paramMap.put("total_fee", "1");
        paramMap.put("spbill_create_ip", "127.0.0.1");
        paramMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
        paramMap.put("trade_type", "NATIVE");
        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
        httpClient.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
        httpClient.setHttps(true);
        httpClient.post();

        Map<String, String> resultMap = WXPayUtil.xmlToMap(httpClient.getContent());

        if (!WXPayConstants.SUCCESS.equals(resultMap.get("result_code"))) {
            throw new YYGHException(20001, "交易错误");
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("codeUrl", resultMap.get("code_url"));
        return map;
    }

    @Override
    public Map<String, String> queryPayStatus(Long orderId) throws Exception {
        OrderInfo orderInfo = orderInfoService.getById(orderId);
        if (orderInfo.getOrderStatus() != 0) {
            throw new YYGHException(20001, "已经支付过了");
        }

        Map<String, String> paramMap = getParamMap(orderInfo.getOutTradeNo());

        //微信查询订单api
        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
        httpClient.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
        httpClient.setHttps(true);
        httpClient.post();

        return WXPayUtil.xmlToMap(httpClient.getContent());
    }

    @Autowired
    private RefundInfoService refundInfoService;

    @Override
    public boolean refund(Long orderId) {
        //保存退款记录
        PaymentInfo paymentInfo = paymentInfoService.getByOrderId(orderId);
        RefundInfo refundInfo = refundInfoService.saveRefundInfo(paymentInfo);

        Map<String, String> paramMap = getParamMap(paymentInfo.getOutTradeNo());
        paramMap.put("out_refund_no", "tk" + paymentInfo.getOutTradeNo());
        paramMap.put("total_fee", "1");
        paramMap.put("refund_fee", "1");

        try {
            //微信退款api
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/secapi/pay/refund");
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
            httpClient.setHttps(true);
            httpClient.setCert(true);
            httpClient.setCertPassword(ConstantPropertiesUtils.PARTNER);
            httpClient.post();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(httpClient.getContent());

            if (CollectionUtils.isNotEmpty(resultMap) && WXPayConstants.SUCCESS.equals(resultMap.get("return_code"))) {
                refundInfo.setTradeNo(resultMap.get("refund_id"));
                refundInfo.setRefundStatus(RefundStatusEnum.REFUND.getStatus());
                refundInfo.setCallbackTime(new Date());
                refundInfo.setCallbackContent(resultMap.toString());
                refundInfoService.updateById(refundInfo);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Map<String, String> getParamMap(String outTradeNo) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", ConstantPropertiesUtils.APPID);
        paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
        paramMap.put("out_trade_no", outTradeNo);
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        paramMap.put("sign", "");
        return paramMap;
    }

}
