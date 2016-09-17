package com.github.allin.controllers;

import com.github.allin.models.User;
import com.github.allin.models.UserDAO;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 */
@Log4j
@SessionAttributes({"user_id"})
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserDAO userDAO;

    @GetMapping("/sign_in")
    public String getSignIn() {
        return "sign_in";
    }
    @PostMapping("/sign_in")
    public String postSignIn(
            @RequestParam("user_mail") String userMail,
            @RequestParam("user_password") String userPassword,
            Model model
    ) {
        User user;
        try {
            user = userDAO.getByMail(userMail);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Wrong user email");
            return "sign_in";
        }
        if (!userPassword.equals(user.getUserPassword())) {
            log.debug("Wrong user password");
            return "sign_in";
        }
        model.addAttribute("user_id", user.getUserID());
        return "redirect:/permission";
    }
}
