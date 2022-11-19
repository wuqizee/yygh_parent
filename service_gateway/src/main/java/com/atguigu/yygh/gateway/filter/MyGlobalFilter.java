package com.atguigu.yygh.gateway.filter;

import com.google.common.net.HttpHeaders;
import com.google.gson.JsonObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author wqz
 * @date 2022/11/8 19:13
 */
//@Component
public class MyGlobalFilter implements GlobalFilter, Ordered {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        if (antPathMatcher.match("/admin/user/login", path)) {
            return chain.filter(exchange);
        }else if (antPathMatcher.match("/admin/**", path)) {
            List<String> token = request.getHeaders().get("X-Token");
            if (token != null && token.get(0).equals("admin-token")) {
                return chain.filter(exchange);
            }
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().set(HttpHeaders.LOCATION, "http://localhost:9528");
            return response.setComplete();
        }else {
            return out(response);
        }
    }

    private Mono<Void> out(ServerHttpResponse response) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("code","20001");
        jsonObject.addProperty("message","路径有误");
        jsonObject.addProperty("success","false");
        byte[] bits =jsonObject.toString().getBytes(StandardCharsets.UTF_8);

        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.NOT_FOUND);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
