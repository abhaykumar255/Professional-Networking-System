package com.professionalnetworking.postsservice.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
    // Will Intercept all the outgoing requests and set the user id in the request header

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Long userId = UserContextHolder.getCurrentUserId();
        if (userId != null) requestTemplate.header("X-USER-ID", userId.toString());
    }
}
