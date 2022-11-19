package com.atguigu.yygh.cmn.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.atguigu.yygh.cmn.test.listener.ExcelListener;
import com.atguigu.yygh.cmn.test.pojo.Student;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author wqz
 * @date 2022/11/2 20:04
 */
public class EasyExcelReadTest {

    public static void main(String[] args) {

    }

    public static void read1() {
        EasyExcel.read("D:\\wqz\\Desktop\\com.atguigu.yygh.test\\excel\\22.xlsx", Student.class, new ExcelListener())
                .sheet(0).doRead();
    }

    public static void read2() {
        ExcelReader excelReader = EasyExcel.read("D:\\wqz\\Desktop\\com.atguigu.yygh.test\\excel\\22.xlsx").build();
        ReadSheet readSheet1 = EasyExcel.readSheet(0).head(Student.class).registerReadListener(new ExcelListener()).build();
        ReadSheet readSheet2 = EasyExcel.readSheet(1).head(Student.class).registerReadListener(new ExcelListener()).build();
        excelReader.read(readSheet1);
        excelReader.read(readSheet2);
        excelReader.finish();
    }

}
