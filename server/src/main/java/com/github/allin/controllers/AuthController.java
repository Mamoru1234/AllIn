package com.github.allin.controllers;

import com.github.allin.models.Client;
import com.github.allin.models.User;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 */
@Log4j
@SessionAttributes({"client_id", "user_id"})
@Controller
public class AuthController {
    @Autowired
    Client.DAO clientDAO;
    @Autowired
    User.DAO userDAO;

    @GetMapping("/auth")
    public String auth(
            @RequestParam("client_id") String clientID,
//            @RequestParam("redirect_url") String redirectURL,
            @SessionAttribute(value = "user_id", required = false) String userID,
            Model model
    ) throws IOException {
        model.addAttribute("client_id", clientID);
        if (userID == null) {
            log.debug("redirect to sign_in");
            return "redirect:/sign_in";
        }
        log.debug("user is already here");
        model.addAttribute("user", userDAO.getByID(userID));
        model.addAttribute("client", clientDAO.getByID(clientID));
        return "auth";
    }
    @GetMapping("/sign_in")
    public String signInGet() {
        return "sign_in";
    }
    @PostMapping("/sign_in")
    public String signInPost(
            @RequestParam("mail") String mail,
            @RequestParam("password") String password,
            @SessionAttribute("client_id") String clientID,
            Model model
    ) {
        User user;
        try {
            user = userDAO.getByMail(mail);
            log.debug("form DB" + user);
        } catch (EmptyResultDataAccessException e) {
            user = User.builder()
                    .userID(UUID.randomUUID().toString())
                    .userMail(mail)
                    .userPassword(password)
                    .build();
            userDAO.insert(user);
            log.debug("inserted" + user);
        }
        model.addAttribute("user_id", user.getUserID());
        return "redirect:/auth?client_id=" + clientID;
    }
}
