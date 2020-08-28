package com.etrans.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMVCConfig implements WebMvcConfigurer {

    // 配置首页
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");//可以访问 http://localhost:8080/ldx/
        registry.addViewController("/index.html").setViewName("index"); //可以访问 http://localhost:8080/ldx/index.html
        registry.addViewController("/main.html").setViewName("dashboard");
        //登录后可以访问 http://localhost:8080/ldx/main.html, 如果没有登录会被拦截
    }

    //自定义国际化组件生效
    @Bean("localeResolver")
    public LocaleResolver localeResolver() {
        return new MyLocalResolver();
    }

    // 添加拦截器,添加放行的路径
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/index.html","/","/user/login","/css/*","/js/*","/img/*");
    }
}
