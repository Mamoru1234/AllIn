package com.github.allin;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 */
@SpringBootApplication
@ComponentScans({
    @ComponentScan("com.github.allin.controllers"),
    @ComponentScan("com.github.allin.models"),
    @ComponentScan("com.github.allin.config"),
    @ComponentScan("com.github.allin.services"),
    @ComponentScan("com.github.allin.advices")
})
public class Server {
    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder()
                .sources(Server.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}