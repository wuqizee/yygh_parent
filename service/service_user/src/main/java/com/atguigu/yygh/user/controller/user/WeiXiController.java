package com.atguigu.yygh.user.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YYGHException;
import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.common.utils.JwtHelper;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.prop.WeiXiProperties;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.user.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * @author wqz
 * @date 2022/11/11 12:57
 */
@Controller
@RequestMapping("/user/weixi")
public class WeiXiController {

    @Autowired
    private WeiXiProperties wxProperties;

    @Autowired
    private UserInfoService userInfoService;

    @ResponseBody
    @GetMapping("/params")
    public R getLoginParam() throws UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<>();
        map.put("appid", wxProperties.getAppid());
        map.put("scope", "snsapi_login");
        map.put("uri", URLEncoder.encode(wxProperties.getUri(), "utf-8"));
        map.put("state", String.valueOf(System.currentTimeMillis()));
        return R.ok().data(map);
    }

    @GetMapping("/callback")
    public String callback(String code, String state) throws Exception {
        // https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");
        String format = String.format(sb.toString(), wxProperties.getAppid(), wxProperties.getSecret(), code);
        JSONObject result = JSONObject.parseObject(HttpClientUtils.get(format));
        String openid = result.getString("openid");
        String accessToken = result.getString("access_token");

        UserInfo userInfo = userInfoService.selectByOpenid(openid);
        if (userInfo == null) {
            // https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
            StringBuilder sb2 = new StringBuilder();
            sb2.append("https://api.weixin.qq.com/sns/userinfo")
                    .append("?access_token=%s")
                    .append("&openid=%s");
            String format2 = String.format(sb2.toString(), accessToken, openid);
            JSONObject result2 = JSONObject.parseObject(HttpClientUtils.get(format2));
            String nickname = result2.getString("nickname");
            userInfo = new UserInfo();
            userInfo.setOpenid(openid);
            userInfo.setNickName(nickname);
            userInfo.setStatus(1);
            userInfoService.save(userInfo);
        }

        if (userInfo.getStatus() == 0) {
            throw new YYGHException(20001, "账号已经锁定");
        }

        HashMap<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(userInfo.getPhone())) {
            map.put("openid", openid);
        }else {
            map.put("openid", "");
        }

        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return "redirect:http://localhost:3000/weixin/callback?token=" + map.get("token")
                + "&openid=" + map.get("openid")
                + "&name=" + URLEncoder.encode(map.get("name"),"utf-8");
    }

}
