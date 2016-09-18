package com.github.allin.client;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.io.IOException;

/**
 */
@SpringBootApplication
public class Server {
    @Bean
    public EmbeddedServletContainerCustomizer customizer() {
        return container -> container.setPort(3000);
    }
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
    public static void main(String... args) {
        new SpringApplicationBuilder()
                .sources(Server.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
