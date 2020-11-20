package com.healthMonitor.fall2020.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 跨域支持
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                 //.allowedMethods("*")
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowedHeaders("*")
					.maxAge(3600 * 24);

        //响应头明显就没包含Access-Control-Allow-Origin：*，跨域了肯定报错了
    }
}