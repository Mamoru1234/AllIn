package com.github.allin.controllers;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 */
@Log4j
@Controller
@SessionAttributes({"client_id", "redirect_url"})
public class AuthController {
    @GetMapping("/oauth/authorize")
    public String authorize(
            @RequestParam("client_id") String clientID,
            @RequestParam("redirect_url") String redirectURL,
            @SessionAttribute(value = "user_id", required = false) String userID,
            Model model
    ){
        model.addAttribute("client_id", clientID);
        model.addAttribute("redirect_url", redirectURL);
        if (userID == null) {
            log.debug("redirect to Sign_in");
            return "redirect:/user/sign_in";
        }
        return "redirect:/user/permission";
    }
}
