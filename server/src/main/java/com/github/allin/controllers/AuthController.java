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
    private static final String sighnInRedirectTemplate = "redirect:/sign_in?client_id=%s";

    @GetMapping("/auth")
    public String auth(
            @RequestParam("client_id") String clientID,
//            @RequestParam("redirect_url") String redirectURL,
            @SessionAttribute(value = "user_id", required = false) String userID
    ) throws IOException {
        System.out.println(userID);
        if (userID == null) {
            return String.format(sighnInRedirectTemplate, clientID);
        }
        return "auth";
    }

    @GetMapping("/sign_in")
    public String permissions(
            @RequestParam("client_id") String clientID,
            HttpServletRequest request
            ) {
        request.getSession().setAttribute("user_id", clientID);
//        Client client = clientDAO.getByID(clientID);
//        modelMap.addAttribute("client", client);
        return "auth";
    }
}
