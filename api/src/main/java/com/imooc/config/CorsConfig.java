package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/** @author afu */
@Configuration
public class CorsConfig {

  public CorsConfig() {}

  @Bean
  public CorsFilter corsFilter() {
    // 添加 cors 配置信息
    CorsConfiguration config = new CorsConfiguration();
    config.addAllowedOrigin("http://localhost:8080");
    // 允许发送 cookie 信息
    config.setAllowCredentials(true);
    // 允许的请求方式
    config.addAllowedMethod("*");
    // 允许的 header，意思就是是否允许获取 header 里面的数据
    config.addAllowedHeader("*");

    // 为 url 添加映射路径
    UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
    corsSource.registerCorsConfiguration("/**", config);

    // 返回定义好的 corsSource
    return new CorsFilter(corsSource);
  }
}
