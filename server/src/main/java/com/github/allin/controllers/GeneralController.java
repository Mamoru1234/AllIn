package com.github.allin.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 */
@Controller
public class GeneralController {
    @GetMapping("/")
    public String getMainPage() {
        return "main";
    }
}
