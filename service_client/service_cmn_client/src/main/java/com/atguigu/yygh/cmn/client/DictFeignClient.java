package com.atguigu.yygh.cmn.client;

import com.atguigu.yygh.cmn.fallback.DictFeignClientFallBack;
import com.atguigu.yygh.model.cmn.Dict;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author wqz
 * @date 2022/11/7 18:31
 */
@FeignClient(value = "service-cmn", path = "/admin/cmn", fallbackFactory = DictFeignClientFallBack.class)
public interface DictFeignClient {

    @GetMapping("/name/{value}")
    public String getNameByValue(@PathVariable("value") Long value);

    @GetMapping("/name/{dictCode}/{value}")
    public String getNameByDictCodeAndValue(@PathVariable("dictCode") String dictCode, @PathVariable("value") Long value);

}
