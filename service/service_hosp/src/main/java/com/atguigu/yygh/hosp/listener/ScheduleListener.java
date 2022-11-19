package com.atguigu.yygh.hosp.listener;

import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.mq.constant.MqConst;
import com.atguigu.yygh.mq.service.RabbitService;
import com.atguigu.yygh.vo.order.OrderMqVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wqz
 * @date 2022/11/16 14:59
 */
@Component
public class ScheduleListener {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private RabbitService rabbitService;

    @RabbitListener(bindings = {
            @QueueBinding(
                    exchange = @Exchange(name = MqConst.EXCHANGE_SCHEDULE, type = "direct"),
                    value = @Queue(name = MqConst.QUEUE_SCHEDULE),
                    key = {MqConst.ROUTING_KEY_SCHEDULE}
            )
    })
    public void consumer(OrderMqVo orderMqVo, Message message, Channel channel) {
        if (orderMqVo.getAvailableNumber() != null) {
            //预约成功后 修改可预约人数并发送短信提醒
            scheduleService.updateAvailableNumber(orderMqVo.getScheduleId(), orderMqVo.getAvailableNumber());
        }else {
            //退款成功后 修改可预约人数加一并发送短信提醒
            String scheduleId = orderMqVo.getScheduleId();
            Schedule schedule = scheduleService.getByScheduleId(scheduleId);
            scheduleService.updateAvailableNumber(orderMqVo.getScheduleId(), schedule.getAvailableNumber() + 1);
        }
        if (orderMqVo.getSmsVo() != null) {
            rabbitService.sendMessage(MqConst.EXCHANGE_SMS, MqConst.ROUTING_KEY_SMS, orderMqVo.getSmsVo());
        }
    }

}
