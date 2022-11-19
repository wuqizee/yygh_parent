package com.atguigu.yygh.user.service;

import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.vo.user.LoginVo;
import com.atguigu.yygh.vo.user.UserAuthVo;
import com.atguigu.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author wqz
 * @since 2022-11-10
 */
public interface UserInfoService extends IService<UserInfo> {

    Map<String, Object> login(LoginVo loginVo);

    UserInfo selectByOpenid(String openid);

    UserInfo getUserInfo(Long userId);

    void auth(UserAuthVo userAuthVo, Long userId);

    Page<UserInfo> getPageList(Integer pageNum, Integer pageSize, UserInfoQueryVo userInfoQueryVo);

    void lock(Long id, Integer status);

    Map<String, Object> show(Long id);

    void approval(Long id, Integer authStatus);

}
