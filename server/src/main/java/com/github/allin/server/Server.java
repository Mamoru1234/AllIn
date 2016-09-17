package com.github.allin.server;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
    @ComponentScan("com.github.allin.server.config"),
    @ComponentScan("com.github.allin.services")
})
public class Server {
    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder()
                .sources(Server.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}