package com.atguigu.yygh.hosp.controller.admin;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.model.hosp.HospitalSet;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author wqz
 * @date 2022/11/1 8:10
 */
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @PostMapping("/login")
    public R login(@RequestBody HospitalSet hospitalSet) {
        return R.ok().data("token", "admin-token");
    }

    @GetMapping("/info")
    public R info(String token) {
        return R.ok().data("roles", Arrays.asList("admin"))
                .data("introduction", "I am a super administrator")
                .data("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif")
                .data("name", "Super Admin");
    }

    @PostMapping("/logout")
    public R logout() {
        return R.ok();
    }

}
