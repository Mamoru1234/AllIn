package com.github.allin.controllers;

import com.github.allin.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 */
@Controller
public class AuthController {
    @Autowired
    Client.DAO clientDAO;

    @GetMapping("/auth")
    public String auth(
            @RequestParam("client_id") String clientID,
//            @RequestParam("redirect_url") String redirectURL,
            @SessionAttribute(value = "user_id", required = false) String userID,
            HttpServletRequest request
    ) throws IOException {
        request.getSession().setAttribute("client_id", clientID);
        if (userID == null) {
            return "redirect:/sign_in";
        }
        return "auth";
    }
}
