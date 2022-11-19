package com.atguigu.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wqz
 * @date 2022/11/3 9:27
 */
public class ExcelListener extends AnalysisEventListener<DictEeVo> {

    private static final int BATCH_COUNT = 100;

    private List<Dict> dictList = new ArrayList<>(BATCH_COUNT);

    private DictMapper baseMapper;

    public ExcelListener(DictMapper dictMapper) {
        baseMapper = dictMapper;
    }

    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo, dict);
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", dict.getId());
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            baseMapper.updateById(dict);
        }else {
            dictList.add(dict);
            //baseMapper.insert(dict);
        }
        if (dictList.size() >= BATCH_COUNT) {
            baseMapper.batchInsert(dictList);
            dictList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (CollectionUtils.isNotEmpty(dictList)) {
            baseMapper.batchInsert(dictList);
        }
    }
}
