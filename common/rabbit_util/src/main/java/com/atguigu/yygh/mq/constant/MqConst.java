package com.atguigu.yygh.mq.constant;

import org.springframework.stereotype.Component;

/**
 * @author wqz
 * @date 2022/11/16 14:29
 */
public class MqConst {

    public static final String EXCHANGE_SCHEDULE = "exchange_schedule";
    public static final String ROUTING_KEY_SCHEDULE = "routing_key_schedule";
    public static final String QUEUE_SCHEDULE = "queue_schedule";

    public static final String EXCHANGE_SMS = "exchange_sms";
    public static final String ROUTING_KEY_SMS = "routing_key_sms";
    public static final String QUEUE_SMS = "queue_sms";

    public static final String EXCHANGE_TASK = "exchange_task";
    public static final String ROUTING_KEY_TASK = "routing_key_task";
    public static final String QUEUE_TASK = "queue_task";

}
