package com.hasim.healthboard.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("**/*.js").addResourceLocations("/static/");
        registry.addResourceHandler("**/*.css").addResourceLocations("/static/");
        registry.addResourceHandler("**/*.png").addResourceLocations("/static/");
        registry.addResourceHandler("**/*.jpeg").addResourceLocations("/static/");
    }
    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:4200");
    }*/
}