package com.atguigu.yygh.cmn.service;

import com.atguigu.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 组织架构表 服务类
 * </p>
 *
 * @author wqz
 * @since 2022-11-02
 */
public interface DictService extends IService<Dict> {

    List<Dict> getDictListByParentId(Long parentId);

    void download(HttpServletResponse response) throws IOException;

    void upload(MultipartFile excelFile) throws IOException;

    String getNameByValue(Long value);

    String getNameByDictCodeAndValue(String dictCode, Long value);

    List<Dict> getDictListByDictCode(String dictCode);

}
