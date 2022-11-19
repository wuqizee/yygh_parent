package com.atguigu.yygh.user.service.impl;

import com.atguigu.yygh.common.exception.YYGHException;
import com.atguigu.yygh.common.utils.JwtHelper;
import com.atguigu.yygh.enums.AuthStatusEnum;
import com.atguigu.yygh.enums.StatusEnum;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.mapper.UserInfoMapper;
import com.atguigu.yygh.user.service.PatientService;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import com.atguigu.yygh.vo.user.UserAuthVo;
import com.atguigu.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author wqz
 * @since 2022-11-10
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        String openid = loginVo.getOpenid();
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YYGHException(20001, "手机或验证码为空");
        }

        String redisCode = redisTemplate.opsForValue().get(phone);
        if (StringUtils.isEmpty(redisCode) || !code.equals(redisCode)) {
            throw new YYGHException(20001, "验证码错误");
        }

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if (!StringUtils.isEmpty(openid)) {
            QueryWrapper<UserInfo> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("openid", openid);
            UserInfo userInfo2 = userInfoMapper.selectOne(queryWrapper2);

            if (userInfo == null) {
                //微信登录后绑定手机
                userInfo = userInfo2;
                userInfo.setPhone(phone);
                userInfoMapper.updateById(userInfo);
            }else {
                //先手机登录过了再微信登录
                userInfo.setNickName(userInfo2.getNickName());
                userInfo.setOpenid(userInfo2.getOpenid());
                userInfoMapper.updateById(userInfo);
                userInfoMapper.deleteById(userInfo2.getId());
            }
        }else if (userInfo == null) {
            //第一次手机登录
            userInfo = new UserInfo();
            userInfo.setPhone(phone);
            userInfo.setStatus(1);
            userInfoMapper.insert(userInfo);
        }
        //第二次及以上登录

        if (userInfo.getStatus() == 0) {
            throw new YYGHException(20001, "账号已经锁定");
        }

        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return map;
    }

    @Override
    public UserInfo selectByOpenid(String openid) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        return userInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public UserInfo getUserInfo(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        packUserInfo(userInfo);
        return userInfo;
    }

    @Override
    public void auth(UserAuthVo userAuthVo, Long userId) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userAuthVo, userInfo);
        userInfo.setId(userId);
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public Page<UserInfo> getPageList(Integer pageNum, Integer pageSize, UserInfoQueryVo userInfoQueryVo) {
        Page<UserInfo> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        String keyword = userInfoQueryVo.getKeyword();
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin();
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd();
        Integer authStatus = userInfoQueryVo.getAuthStatus();

        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.and(qw->qw.like(UserInfo::getName, keyword).or().eq(UserInfo::getPhone, keyword));
        }
        if (!StringUtils.isEmpty(createTimeBegin)) {
            queryWrapper.ge(UserInfo::getCreateTime, createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)) {
            queryWrapper.le(UserInfo::getCreateTime, createTimeEnd);
        }
        if (!StringUtils.isEmpty(authStatus)) {
            queryWrapper.eq(UserInfo::getAuthStatus, authStatus);
        }
        userInfoMapper.selectPage(page, queryWrapper);
        page.getRecords().forEach(item->{
            packUserInfo(item);
        });
        return page;
    }

    private void packUserInfo(UserInfo userInfo) {
        Integer authStatus = userInfo.getAuthStatus();
        userInfo.getParam().put("authStatusString", AuthStatusEnum.getStatusNameByStatus(authStatus));
        Integer status = userInfo.getStatus();
        userInfo.getParam().put("statusString", StatusEnum.getStatusNameByStatus(status));
    }

    @Override
    public void lock(Long id, Integer status) {
        if (status == 0 || status == 1) {
            UserInfo userInfo = userInfoMapper.selectById(id);
            userInfo.setStatus(status);
            userInfoMapper.updateById(userInfo);
        }
    }

    @Autowired
    private PatientService patientService;

    @Override
    public Map<String, Object> show(Long id) {
        HashMap<String, Object> map = new HashMap<>();
        UserInfo userInfo = userInfoMapper.selectById(id);
        map.put("userInfo", userInfo);
        List<Patient> patientList = patientService.findListByUserId(id);
        map.put("patientList", patientList);
        return map;
    }

    @Override
    public void approval(Long id, Integer authStatus) {
        if (authStatus == 2 || authStatus == -1) {
            UserInfo userInfo = userInfoMapper.selectById(id);
            userInfo.setAuthStatus(authStatus);
            userInfoMapper.updateById(userInfo);
        }
    }
}
