package com.atguigu.yygh.task.job;

import com.atguigu.yygh.mq.constant.MqConst;
import com.atguigu.yygh.mq.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author wqz
 * @date 2022/11/18 18:18
 */
@Component
public class PatientRemind {

    @Autowired
    private RabbitService rabbitService;

    @Scheduled(cron = "*0 0 7 * * ?")
    public void patientTips() {
        rabbitService.sendMessage(MqConst.EXCHANGE_TASK, MqConst.ROUTING_KEY_TASK, "");
    }

}
