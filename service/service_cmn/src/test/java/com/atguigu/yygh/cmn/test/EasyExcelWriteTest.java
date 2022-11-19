package com.atguigu.yygh.cmn.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.atguigu.yygh.cmn.test.pojo.Student;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wqz
 * @date 2022/11/2 19:07
 */
public class EasyExcelWriteTest {

    public static void main(String[] args) {

    }

    private static List data() {
        List<Student> list = new ArrayList<>();
        list.add(new Student("张三", 18, true));
        list.add(new Student("李四", 28, false));
        list.add(new Student("王五", 38, true));
        return list;
    }

    public static void write1() {
        EasyExcel.write("D:\\wqz\\Desktop\\com.atguigu.yygh.test\\excel\\11.xlsx", Student.class)
                .sheet(0, "学生管理表").doWrite(data());
    }

    public static void write2() {
        ExcelWriter excelWriter = EasyExcel.write("D:\\wqz\\Desktop\\com.atguigu.yygh.test\\excel\\22.xlsx", Student.class).build();
        WriteSheet writeSheet1 = EasyExcel.writerSheet(0, "01表").build();
        WriteSheet writeSheet2 = EasyExcel.writerSheet(1, "02表").build();
        excelWriter.write(data(), writeSheet1);
        excelWriter.write(data(), writeSheet2);
        excelWriter.finish();
    }

    public static void write3() {
        ExcelWriter excelWriter = EasyExcel.write("D:\\wqz\\Desktop\\com.atguigu.yygh.test\\excel\\33.xlsx").build();
        WriteSheet writeSheet1 = EasyExcel.writerSheet(0, "1号表").head(Student.class).build();
        WriteSheet writeSheet2 = EasyExcel.writerSheet(1, "2号表").head(Student.class).build();
        excelWriter.write(data(), writeSheet1);
        excelWriter.write(data(), writeSheet2);
        excelWriter.finish();
    }

}
