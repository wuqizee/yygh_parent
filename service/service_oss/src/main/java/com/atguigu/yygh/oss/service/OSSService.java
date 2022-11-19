package com.atguigu.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author wqz
 * @date 2022/11/12 15:21
 */
public interface OSSService {

    String upload(MultipartFile file);

}
