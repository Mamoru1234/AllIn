package com.github.allin.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 */
@Controller
public class MainController {
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String hello(
            @RequestParam("client_id") String clientID,
            @RequestParam("redirect_url")String redirectURL,
            ModelMap modelMap
    ) throws IOException {
        Service service = Service.builder()
                .serviceName("my service")
                .build();
        modelMap.addAttribute("service", service);
        return "auth";
    }
}
