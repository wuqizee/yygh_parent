package com.atguigu.yygh.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YYGHException;
import com.atguigu.yygh.enums.OrderStatusEnum;
import com.atguigu.yygh.enums.PaymentStatusEnum;
import com.atguigu.yygh.hosp.client.ScheduleFeignClient;
import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.model.order.PaymentInfo;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.mq.constant.MqConst;
import com.atguigu.yygh.mq.service.RabbitService;
import com.atguigu.yygh.order.mapper.OrderInfoMapper;
import com.atguigu.yygh.order.service.OrderInfoService;
import com.atguigu.yygh.order.service.PaymentInfoService;
import com.atguigu.yygh.order.service.WeiXiService;
import com.atguigu.yygh.order.utils.HttpRequestHelper;
import com.atguigu.yygh.user.client.UserFeignClient;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import com.atguigu.yygh.vo.order.OrderCountQueryVo;
import com.atguigu.yygh.vo.order.OrderCountVo;
import com.atguigu.yygh.vo.sms.SmsVo;
import com.atguigu.yygh.vo.order.OrderMqVo;
import com.atguigu.yygh.vo.order.OrderQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author wqz
 * @since 2022-11-15
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Autowired
    private ScheduleFeignClient scheduleFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private RabbitService rabbitService;

    @Override
    public Long submitOrder(String scheduleId, Long patientId) {
        OrderInfo orderInfo = new OrderInfo();
        //1.根据就诊人id获取就诊人信息
        Patient patient = userFeignClient.getPatientById(patientId);
        //2.根据排版id获取排版信息
        ScheduleOrderVo scheduleOrderVo = scheduleFeignClient.getScheduleOrderVoById(scheduleId);

        if (new DateTime(scheduleOrderVo.getStopTime()).isBeforeNow()) {
            throw new YYGHException(20001, "挂号失败");
        }

        //3.在平台请求第三方医院，确认是否可以挂号
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode", scheduleOrderVo.getHoscode());
        paramMap.put("depcode", scheduleOrderVo.getDepcode());
        paramMap.put("hosScheduleId", scheduleOrderVo.getHosScheduleId());
        paramMap.put("reserveDate", scheduleOrderVo.getReserveDate());
        paramMap.put("reserveTime", scheduleOrderVo.getReserveTime());
        paramMap.put("amount", scheduleOrderVo.getAmount());
        JSONObject respone =
                HttpRequestHelper.sendRequest(paramMap, "http://localhost:9998/order/submitOrder");

        //3.2如果可以
        if (respone != null && respone.getIntValue("code") == 200) {
            //3.2.1把上述三部分数据添加到数据表中
            orderInfo.setPatientId(patientId);
            orderInfo.setUserId(patient.getUserId());
            orderInfo.setPatientName(patient.getName());
            orderInfo.setPatientPhone(patient.getPhone());

            orderInfo.setHoscode(scheduleOrderVo.getHoscode());
            orderInfo.setHosname(scheduleOrderVo.getHosname());
            orderInfo.setDepcode(scheduleOrderVo.getDepcode());
            orderInfo.setDepname(scheduleOrderVo.getDepname());
            orderInfo.setScheduleId(scheduleId);
            orderInfo.setTitle(scheduleOrderVo.getTitle());
            orderInfo.setReserveDate(scheduleOrderVo.getReserveDate());
            orderInfo.setReserveTime(scheduleOrderVo.getReserveTime());
            orderInfo.setAmount(scheduleOrderVo.getAmount());
            orderInfo.setQuitTime(scheduleOrderVo.getQuitTime());

            JSONObject data = respone.getJSONObject("data");
            orderInfo.setHosRecordId(data.getString("hosRecordId"));
            orderInfo.setNumber(data.getInteger("number"));
            orderInfo.setFetchTime(data.getString("fetchTime"));
            orderInfo.setFetchAddress(data.getString("fetchAddress"));
            orderInfo.setOutTradeNo(System.currentTimeMillis() + "" + new Random().nextInt(100));
            orderInfo.setOrderStatus(OrderStatusEnum.UNPAID.getStatus());

            baseMapper.insert(orderInfo);


            //3.2.2更新可预约数
            //3.2.3给就诊人发送预约成功的短信提示
            Integer availableNumber = data.getInteger("availableNumber");

            HashMap<String, Object> param = new HashMap<>();
            param.put("name", patient.getName());
            param.put("reserveDate", scheduleOrderVo.getReserveDate());
            param.put("reserveTime", scheduleOrderVo.getReserveTime());
            param.put("hosname", scheduleOrderVo.getHosname());
            param.put("fetchAddress", data.getString("fetchAddress"));

            SmsVo smsVo = new SmsVo();
            smsVo.setPhone(patient.getPhone());
            smsVo.setTemplateCode("预约成功");

            OrderMqVo orderMqVo = new OrderMqVo();
            orderMqVo.setScheduleId(scheduleId);
            orderMqVo.setAvailableNumber(availableNumber);
            orderMqVo.setSmsVo(smsVo);

            rabbitService.sendMessage(MqConst.EXCHANGE_SCHEDULE, MqConst.ROUTING_KEY_SCHEDULE, orderMqVo);
        }else {
            //3.1如果不可以抛异常
            throw new YYGHException(20001, "下单失败");
        }

        //4放回订单id
        return orderInfo.getId();
    }


    @Override
    public Page<OrderInfo> getPageList(Integer pageNum, Integer pageSize, OrderQueryVo orderQueryVo) {
        Long userId = orderQueryVo.getUserId();
        Long patientId = orderQueryVo.getPatientId();
        String keyword = orderQueryVo.getKeyword();
        String orderStatus = orderQueryVo.getOrderStatus();
        String reserveDate = orderQueryVo.getReserveDate();
        String createTimeBegin = orderQueryVo.getCreateTimeBegin();
        String createTimeEnd = orderQueryVo.getCreateTimeEnd();

        Page<OrderInfo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like("hosname", keyword);
        }
        if (!StringUtils.isEmpty(patientId)) {
            queryWrapper.eq("patient_id", patientId);
        }
        if (!StringUtils.isEmpty(orderStatus)) {
            queryWrapper.eq("order_status", orderStatus);
        }
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.eq("reserve_date", reserveDate);
        }
        if (!StringUtils.isEmpty(createTimeBegin)) {
            queryWrapper.ge("create_time", createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)) {
            queryWrapper.le("create_time", createTimeEnd);
        }
        baseMapper.selectPage(page, queryWrapper);
        page.getRecords().forEach(this::packOrderInfo);
        return page;
    }

    private void packOrderInfo(OrderInfo orderInfo) {
        String orderStatusString = OrderStatusEnum.getStatusNameByStatus(orderInfo.getOrderStatus());
        orderInfo.getParam().put("orderStatusString", orderStatusString);
    }

    @Override
    public OrderInfo getOrders(String orderId) {
        OrderInfo orderInfo = baseMapper.selectById(orderId);
        packOrderInfo(orderInfo);
        return orderInfo;
    }

    @Override
    public OrderInfo updateStatus(String outTradeNo, Integer status) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("out_trade_no", outTradeNo);
        OrderInfo orderInfo = baseMapper.selectOne(queryWrapper);

        orderInfo.setOrderStatus(status);
        baseMapper.updateById(orderInfo);
        return orderInfo;
    }

    @Autowired
    private WeiXiService weiXiService;

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Transactional
    @Override
    public boolean cancelOrder(Long orderId) {
        //1是否超过退款的时间
        OrderInfo orderInfo = baseMapper.selectById(orderId);
        DateTime quitTime = new DateTime(orderInfo.getQuitTime());
        if (quitTime.isBeforeNow()) {
            throw new YYGHException(20001, "已经超过预约截至的时间");
        }

        //2通知第三方医院
        Map<String, Object> map = new HashMap<>();
        map.put("hoscode", orderInfo.getHoscode());
        map.put("hosRecordId", orderInfo.getHosRecordId());
        JSONObject response = HttpRequestHelper.sendRequest(map, "http://localhost:9998/order/updateCancelStatus");
        if (response == null || response.getIntValue("code") != 200) {
            throw new YYGHException(20001, "取消预约失败");
        }

        //3是否付款
        if (orderInfo.getOrderStatus().intValue() == OrderStatusEnum.PAID.getStatus()) {
            boolean flag = weiXiService.refund(orderId);
            if (!flag) {
                throw new YYGHException(20001, "退款失败");
            }
        }

        //4取消预约 修改订单状态和支付记录状态
        orderInfo.setOrderStatus(OrderStatusEnum.CANCLE.getStatus());
        baseMapper.updateById(orderInfo);

        PaymentInfo paymentInfo = paymentInfoService.getByOrderId(orderId);
        if (paymentInfo != null) {
            paymentInfo.setPaymentStatus(PaymentStatusEnum.REFUND.getStatus());
            paymentInfoService.updateById(paymentInfo);
        }

        //5修改可预约人数
        //6发送短信提醒
        SmsVo smsVo = new SmsVo();
        smsVo.setPhone(orderInfo.getPatientPhone());
        smsVo.setTemplateCode("退款成功");

        OrderMqVo orderMqVo = new OrderMqVo();
        orderMqVo.setScheduleId(orderInfo.getScheduleId());
        orderMqVo.setSmsVo(smsVo);

        rabbitService.sendMessage(MqConst.EXCHANGE_SCHEDULE, MqConst.ROUTING_KEY_SCHEDULE, orderMqVo);
        return true;
    }

    @Override
    public void patientTips() {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("reserve_date", new DateTime().toString("yyyy-MM-dd"));
        queryWrapper.ne("order_status", OrderStatusEnum.CANCLE.getStatus());
        List<OrderInfo> orderInfoList = baseMapper.selectList(queryWrapper);
        for (OrderInfo orderInfo : orderInfoList) {
            SmsVo smsVo = new SmsVo();
            smsVo.setPhone(orderInfo.getPatientPhone());
            smsVo.setTemplateCode("就医提醒");
            rabbitService.sendMessage(MqConst.EXCHANGE_SMS, MqConst.ROUTING_KEY_SMS, smsVo);
        }
    }

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Override
    public Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo) {
        List<OrderCountVo> orderCountVoList = orderInfoMapper.selectOrderCount(orderCountQueryVo);
        List<String> reserveDateList = orderCountVoList.stream()
                .map(OrderCountVo::getReserveDate).collect(Collectors.toList());
        List<Integer> countList = orderCountVoList.stream()
                .map(OrderCountVo::getCount).collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        map.put("dateList", reserveDateList);
        map.put("countList", countList);
        return map;
    }
}
