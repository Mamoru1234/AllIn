package com.github.allin.controllers;

import com.github.allin.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class UserController {
    @Autowired
    User.DAO userDAO;
    @GetMapping("/sign_in")
    public String signInGet() {
        return "sign_in";
    }
    @PostMapping("/sign_in")
    public String signInPost(
            @RequestParam("mail") String mail,
            @RequestParam("password") String password,
            @SessionAttribute("client_id") String clientID,
            HttpServletRequest request
    ) {
        User user;
        try {
            user = userDAO.getByMail(mail);
        } catch (EmptyResultDataAccessException e) {
            user = User.builder()
                    .userID(UUID.randomUUID().toString())
                    .userMail(mail)
                    .userPassword(password)
                    .build();
            userDAO.insert(user);
        }
        request.getSession().setAttribute("user_id", user.getUserID());
        return "redirect:/auth?client_id=" + clientID;
    }
}
