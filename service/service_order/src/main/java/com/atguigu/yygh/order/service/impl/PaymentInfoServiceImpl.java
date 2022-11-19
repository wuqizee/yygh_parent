package com.atguigu.yygh.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YYGHException;
import com.atguigu.yygh.enums.OrderStatusEnum;
import com.atguigu.yygh.enums.PaymentStatusEnum;
import com.atguigu.yygh.enums.PaymentTypeEnum;
import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.model.order.PaymentInfo;
import com.atguigu.yygh.order.mapper.PaymentInfoMapper;
import com.atguigu.yygh.order.service.OrderInfoService;
import com.atguigu.yygh.order.service.PaymentInfoService;
import com.atguigu.yygh.order.utils.HttpRequestHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/16 20:42
 */
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {

    @Autowired
    private OrderInfoService orderInfoService;

    @Override
    public void savePaymentInfo(OrderInfo orderInfo) {
        QueryWrapper<PaymentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderInfo.getId());
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            return;
        }
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOutTradeNo(orderInfo.getOutTradeNo());
        paymentInfo.setOrderId(orderInfo.getId());
        paymentInfo.setPaymentType(PaymentTypeEnum.WEIXIN.getStatus());
        paymentInfo.setTotalAmount(orderInfo.getAmount());
        String subject = new DateTime(orderInfo.getReserveDate())
                .toString("yyyy-MM-dd")+"|"+orderInfo.getHosname()+"|"+orderInfo.getDepname()+"|"+orderInfo.getTitle();
        paymentInfo.setSubject("看病");
        paymentInfo.setPaymentStatus(PaymentStatusEnum.UNPAID.getStatus());
        baseMapper.insert(paymentInfo);
    }

    @Override
    public void paySuccess(Map<String, String> resultMap) {
        //更新订单的订单状态
        String outTradeNo = resultMap.get("out_trade_no");
        OrderInfo orderInfo = orderInfoService.updateStatus(outTradeNo, OrderStatusEnum.PAID.getStatus());

        //更新支付交易的状态及微信返回trade_no
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setTradeNo(resultMap.get("transaction_id"));
        paymentInfo.setPaymentStatus(PaymentStatusEnum.PAID.getStatus());
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent(resultMap.toString());
        UpdateWrapper<PaymentInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("out_trade_no", resultMap.get("out_trade_no"));
        baseMapper.update(paymentInfo, updateWrapper);

        //通知第三方医院
        Map<String, Object> map = new HashMap<>();
        map.put("hoscode", orderInfo.getHoscode());
        map.put("hosRecordId", orderInfo.getHosRecordId());
        JSONObject respone = HttpRequestHelper.sendRequest(map, "http://localhost:9998/order/updatePayStatus");

        if (respone == null || respone.getIntValue("code") != 200) {
            throw new YYGHException(20001, "支付失败");
        }
    }

    @Override
    public PaymentInfo getByOrderId(Long orderId) {
        QueryWrapper<PaymentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        return baseMapper.selectOne(queryWrapper);
    }
}
