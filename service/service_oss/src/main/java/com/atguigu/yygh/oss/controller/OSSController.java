package com.atguigu.yygh.oss.controller;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.oss.service.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wqz
 * @date 2022/11/12 15:09
 */
@RestController
@RequestMapping("/user/oss")
public class OSSController {

    @Autowired
    private OSSService ossService;

    @PostMapping("/upload")
    public R upload(MultipartFile file) {
        String url = ossService.upload(file);
        return R.ok().data("url", url);
    }

}
