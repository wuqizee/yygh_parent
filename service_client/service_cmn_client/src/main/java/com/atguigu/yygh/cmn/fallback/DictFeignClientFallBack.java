package com.atguigu.yygh.cmn.fallback;

import com.atguigu.yygh.cmn.client.DictFeignClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author wqz
 * @date 2022/11/15 17:52
 */
@Component
public class DictFeignClientFallBack implements FallbackFactory<DictFeignClient> {

    @Override
    public DictFeignClient create(Throwable throwable) {
        return new DictFeignClient() {
            @Override
            public String getNameByValue(Long value) {
                return "服务器升级中，请稍后重试1";
            }

            @Override
            public String getNameByDictCodeAndValue(String dictCode, Long value) {
                return "服务器升级中，请稍后重试2";
            }
        };
    }

}
