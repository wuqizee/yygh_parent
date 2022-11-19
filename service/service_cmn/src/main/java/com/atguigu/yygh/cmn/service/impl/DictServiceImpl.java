package com.atguigu.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.cmn.listener.ExcelListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 组织架构表 服务实现类
 * </p>
 *
 * @author wqz
 * @since 2022-11-02
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    @Cacheable(value = "dict")
    public List<Dict> getDictListByParentId(Long parentId) {
        List<Dict> dictList = getDictListByPid(parentId);
        for (Dict dict : dictList) {
            dict.setHasChildren(isHasChildren(dict.getId()));
        }
        return dictList;
    }

    private List<Dict> getDictListByPid(Long parentId) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        return baseMapper.selectList(queryWrapper);
    }

    private boolean isHasChildren(Long id) {
        QueryWrapper<Dict> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("parent_id", id);
        return baseMapper.selectCount(queryWrapper2) > 0;
    }

    @Override
    public void download(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("数据字典", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "parent_id", "name", "value", "dict_code");
        List<Dict> dictList = baseMapper.selectList(queryWrapper);
        List<DictEeVo> dictEeVoList = new ArrayList<>(dictList.size());
        for (Dict dict : dictList) {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictEeVo);
            dictEeVoList.add(dictEeVo);
        }

        EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("模板").doWrite(dictEeVoList);
    }

    @Override
    @CacheEvict(value = "dict", allEntries = true)
    public void upload(MultipartFile excelFile) throws IOException {
        EasyExcel.read(excelFile.getInputStream(), DictEeVo.class, new ExcelListener(baseMapper)).sheet().doRead();
    }

    @Override
    public String getNameByValue(Long value) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("value", value);
        Dict dict = baseMapper.selectOne(queryWrapper);
        return dict.getName();
    }

    @Override
    public String getNameByDictCodeAndValue(String dictCode,Long value) {
        Dict dict1 = getDictByDictCode(dictCode);
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", dict1.getId()).eq("value", value);
        Dict dict2 = baseMapper.selectOne(queryWrapper);
        return dict2.getName();
    }

    private Dict getDictByDictCode(String dictCode) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_code", dictCode);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Dict> getDictListByDictCode(String dictCode) {
        Dict dict = getDictByDictCode(dictCode);
        return getDictListByPid(dict.getId());
    }

}
