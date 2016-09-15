package com.github.allin.server;

import com.github.allin.server.config.FreeMarkerConfig;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 */
@EnableAutoConfiguration
@ComponentScans({
    @ComponentScan("com.github.allin.controllers"),
    @ComponentScan("com.github.allin.models"),
    @ComponentScan("com.github.allin.server.config")
})
public class Server {
    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder()
                .sources(Server.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}