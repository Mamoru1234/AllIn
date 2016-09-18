package com.github.allin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.io.IOException;


@Configuration
public class FreeMarkerConfig {
    @Bean
    public FreeMarkerViewResolver viewResolver()
    {
        FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
        viewResolver.setExposeSpringMacroHelpers(true);
        viewResolver.setExposeRequestAttributes(true);
        viewResolver.setPrefix("");
        viewResolver.setSuffix(".ftl");
        viewResolver.setContentType("text/html;charset=UTF-8");
        viewResolver.setCache(false);
        return viewResolver;
    }
    @Bean(name = "freemarkerConfig")
    public FreeMarkerConfigurer getFreemarkerConfig() throws IOException {
        FreeMarkerConfigurer result = new FreeMarkerConfigurer();
        result.setTemplateLoaderPath("/views");
        return result;
    }
}
