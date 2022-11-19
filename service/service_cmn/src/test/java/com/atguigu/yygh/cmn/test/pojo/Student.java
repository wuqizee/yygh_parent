package com.atguigu.yygh.cmn.test.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wqz
 * @date 2022/11/2 19:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @ExcelProperty("姓名")
    @ColumnWidth(10)
    private String name;

    @ExcelProperty("年龄")
    private Integer age;

    @ExcelProperty("性别")
    private Boolean gender;

}
