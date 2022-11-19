package com.atguigu.yygh.sms.service.impl;

import com.atguigu.yygh.sms.service.SMSService;
import com.atguigu.yygh.sms.utils.RandomUtil;
import com.atguigu.yygh.sms.utils.HttpUtils;
import com.atguigu.yygh.vo.sms.SmsVo;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wqz
 * @date 2022/11/10 16:39
 */
@Service
public class SMSServiceImpl implements SMSService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean sendCode(String phone) {
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return true;
        }

        String host = "http://dingxin.market.alicloudapi.com";
        String path = "/dx/sendSms";
        String method = "POST";
        String appcode = "ff052384ba17415aa296d9d8a255ed3f";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        String fourBitRandom = RandomUtil.getFourBitRandom();
        querys.put("param", "code:" + fourBitRandom);
        querys.put("tpl_id", "TP1711063");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));

            redisTemplate.opsForValue().set(phone, fourBitRandom, 30, TimeUnit.DAYS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void sendMessage(SmsVo smsVo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date());
        System.out.println(currentTime + ":" + smsVo.getTemplateCode() + "->" + smsVo.getPhone());
        System.out.println("====================================");
    }
}
