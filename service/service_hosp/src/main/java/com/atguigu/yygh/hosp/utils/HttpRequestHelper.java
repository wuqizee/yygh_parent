package com.atguigu.yygh.hosp.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wqz
 * @date 2022/11/5 9:56
 */
public class HttpRequestHelper {

    public static Map<String, String> switchMap(Map<String, String[]> paramMap) {
        HashMap<String, String> map = new HashMap<>();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue()[0];
            map.put(key, value);
        }
        return map;
    }

}
