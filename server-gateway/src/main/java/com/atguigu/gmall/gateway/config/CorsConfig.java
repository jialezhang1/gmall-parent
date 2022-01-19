package com.atguigu.gmall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
/**
 * @author jiale
 * @date 2022/1/12 - 13:12
 */
@Configuration
public class CorsConfig {

    //  设置过滤对象,将其放入spring 容器中！
    @Bean
    public CorsWebFilter corsWebFilter(){

        //  CorsConfiguration
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);    //  可以携带cookie
        corsConfiguration.addAllowedOrigin("*");    //  允许跨域
        corsConfiguration.addAllowedMethod("*");    //  设置请求方法
        corsConfiguration.addAllowedHeader("*");    //  设置请求头
        //  CorsConfigurationSource
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        //  返回对象
        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }


}
