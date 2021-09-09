package com.xpp.book;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "author", fallback = AuthorFeignClientFallback.class, configuration = FeignConfiguration.class)
public interface AuthorFeignClient {
    @GetMapping("getAuthorName")
    String getAuthorName();
}
class FeignConfiguration {
    @Bean
    public AuthorFeignClientFallback echoServiceFallback() {
        return new AuthorFeignClientFallback();
    }
}

class AuthorFeignClientFallback implements AuthorFeignClient {

    @Override
    public String getAuthorName() {
        return "author服务出现问题或者请求超时。走熔断逻辑";
    }
}
