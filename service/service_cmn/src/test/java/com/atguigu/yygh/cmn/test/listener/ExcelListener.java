package com.atguigu.yygh.cmn.test.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.yygh.cmn.test.pojo.Student;

import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/2 20:07
 */
public class ExcelListener extends AnalysisEventListener<Student> {
    @Override
    public void invokeHeadMap(Map headMap, AnalysisContext context) {
        System.out.println("excel头" + headMap);
    }

    @Override
    public void invoke(Student student, AnalysisContext analysisContext) {
        System.out.println(student);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("解析完毕,处理后续工作");
        System.out.println("===================");
    }
}
