package com.atguigu.yygh.cmn.mapper;

import com.atguigu.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 组织架构表 Mapper 接口
 * </p>
 *
 * @author wqz
 * @since 2022-11-02
 */
public interface DictMapper extends BaseMapper<Dict> {

    @Insert("<script>"
            +"insert dict(id, parent_id, name, value, dict_code)"
            +"<foreach collection='dictList' item='dict' separator=',' open='values'>"
            +"(#{dict.id},#{dict.parentId},#{dict.name},#{dict.value},#{dict.dictCode})"
            +"</foreach>"
            +"</script>")
    void batchInsert(@Param("dictList") List<Dict> dictList);

}
