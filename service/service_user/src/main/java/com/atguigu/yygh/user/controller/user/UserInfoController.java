package com.atguigu.yygh.user.controller.user;


import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.common.utils.JwtHelper;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import com.atguigu.yygh.vo.user.UserAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author wqz
 * @since 2022-11-10
 */
@RestController
@RequestMapping("/user/info")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/login")
    public R login(@RequestBody LoginVo loginVo) {
        Map<String, Object> map = userInfoService.login(loginVo);
        return R.ok().data(map);
    }

    @GetMapping("/auth/getUserInfo")
    public R show(@RequestHeader String token) {
        Long userId = JwtHelper.getUserId(token);
        UserInfo userInfo = userInfoService.getUserInfo(userId);
        return R.ok().data("userInfo", userInfo);
    }

    @PostMapping("/auth/userAuth")
    public R auth(@RequestBody UserAuthVo userAuthVo, @RequestHeader String token) {
        Long userId = JwtHelper.getUserId(token);
        userInfoService.auth(userAuthVo, userId);
        return R.ok();
    }

}

