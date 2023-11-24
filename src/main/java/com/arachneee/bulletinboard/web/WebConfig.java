package com.arachneee.bulletinboard.web;

import com.arachneee.bulletinboard.web.argumentresolver.LoginMemberArgumentResolver;
import com.arachneee.bulletinboard.web.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginCheckInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/", "/members/add", "/login", "/logout",
//                        "/css/**", "/*.ico", "/error"
//                );
//    }
//
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(new LoginMemberArgumentResolver());
//    }
}
