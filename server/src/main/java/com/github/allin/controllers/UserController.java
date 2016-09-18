package com.github.allin.controllers;

import com.github.allin.models.User;
import com.github.allin.models.UserDAO;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.UUID;

/**
 */
@Log4j
@SessionAttributes({"user_id"})
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserDAO userDAO;

    @GetMapping("/sign_in")
    public String getSignIn() {
        log.debug("get sign_in page");
        return "sign_in";
    }
    @PostMapping("/sign_in")
    public String postSignIn(
            @RequestParam("user_mail") String userMail,
            @RequestParam("user_password") String userPassword,
            @SessionAttribute(value = "client_id", required = false) String client_id,
            Model model
    ) {
        User user;
        try {
            user = userDAO.getByMail(userMail);
            log.debug("user from DB: " + user);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Wrong user email");
            model.addAttribute("wrong_input", true);
            return "sign_in";
        }
        if (!userPassword.equals(user.getUserPassword())) {
            log.debug("Wrong user password");
            model.addAttribute("wrong_input", true);
            return "sign_in";
        }
        model.addAttribute("user_id", user.getUserID());
        if (client_id != null) {
            return "redirect:/user/permission";
        }
        return "welcome";
    }

    @GetMapping("/sign_up")
    public String userSignUpGet(
            @SessionAttribute(value = "client_id", required = false) String clientID
    ) {
        if (clientID == null) {
            return "sign_up";
        }
        return "welcome";
    }

    @PostMapping("/sign_up")
    public String userSignUpForm(
            @RequestParam("user_mail") String userMail,
            @RequestParam("user_pass") String userPass,
            Model model
    ) {
        User user = User.builder()
                .userID(UUID.randomUUID().toString())
                .userMail(userMail)
                .userPassword(userPass)
                .build();
        try {
            userDAO.insert(user);
            model.addAttribute("user_id", user.getUserID());
            return "redirect:/";
        }catch (DataIntegrityViolationException e) {
            log.error("data error: ", e);
            model.addAttribute("err_mail", true);
            return "sign_up";
        }
    }
}
