package com.atguigu.yygh.user.controller.admin;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/14 11:04
 */
@RestController
@RequestMapping("/admin/info")
public class AdminUserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/getPageList/{pageNum}/{pageSize}")
    public R getPageList(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                         @RequestBody UserInfoQueryVo userInfoQueryVo) {
        Page<UserInfo> page = userInfoService.getPageList(pageNum, pageSize, userInfoQueryVo);
        return R.ok().data("page", page);
    }

    @PutMapping("/lock/{id}/{status}")
    public R lock(@PathVariable Long id, @PathVariable Integer status) {
        userInfoService.lock(id, status);
        return R.ok();
    }

    @GetMapping("/show/{id}")
    public R show(@PathVariable Long id) {
        Map<String, Object> map = userInfoService.show(id);
        return R.ok().data(map);
    }

    @PutMapping("/approval/{id}/{authStatus}")
    public R approval(@PathVariable Long id, @PathVariable Integer authStatus) {
        userInfoService.approval(id, authStatus);
        return R.ok();
    }
}
