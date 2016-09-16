package com.github.allin.controllers;

import com.github.allin.models.Client;
import com.github.allin.models.Permission;
import com.github.allin.models.User;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 */
@Log4j
@SessionAttributes({"client_id", "user_id", "redirect_url"})
@Controller
public class AuthController {
    @Autowired
    private Client.DAO clientDAO;
    @Autowired
    private User.DAO userDAO;
    @Autowired
    private Permission.DAO permissionDAO;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    @GetMapping("/auth")
    public String auth(
            @RequestParam("client_id") String clientID,
            @RequestParam("redirect_url") String redirectURL,
            @SessionAttribute(value = "user_id", required = false) String userID,
            Model model
    ) throws IOException {
        log.debug("redirect_url: " + redirectURL);
        model.addAttribute("client_id", clientID);
        model.addAttribute("redirect_url", redirectURL);
        if (userID == null) {
            log.debug("redirect to sign_in");
            return "redirect:/sign_in";
        }
        log.debug("user is already here");
        User user = userDAO.getByID(userID);
        log.debug(user);
        model.addAttribute("user", user);
        Client client = clientDAO.getByID(clientID);
        log.debug(client);
        model.addAttribute("client", client);
        return "auth";
    }

    @PostMapping("/token")
    @ResponseBody
    public Map<String, String> getToken(
            @RequestParam("client_id") String clientID,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("grant_token") String grantToken
    ) {
        Map<String, String> result = new HashMap<>();
        Client client = clientDAO.getByID(clientID);
        if (client == null || clientSecret == null) {
//            TODO fail response
            log.error("not found user");
        }
        if (clientSecret == null || !clientSecret.equals(client.getClientSecret())) {
//            TODO fail wrong pass
            log.error("wrong pass");
        }
        String userID = valueOperations.get(grantToken);
        if (userID == null) {
//            TODO fail response
            log.error("not found grant");
        }
//      invalidate grant token
        valueOperations.set(grantToken, null, 1, TimeUnit.MILLISECONDS);
        Permission permission = Permission.builder()
                .accessToken(UUID.randomUUID().toString())
                .clientID(clientID)
                .userID(userID)
                .build();
        permissionDAO.insert(permission);
        result.put("access_token", permission.getAccessToken());
        return result;
    }

    @PostMapping("/auth")
    public void authAllow(
            @RequestParam("client_id") String clientID,
            @RequestParam("user_id") String userID,
            @SessionAttribute("redirect_url") String redirectURL,
            HttpServletResponse response
    ) throws IOException {
        log.debug("clientID: " + clientID);
        log.debug("userID: " + userID);
        String grantToken = "grant_token";
        log.debug("grant: " + grantToken);
        valueOperations.set(grantToken, userID, 1, TimeUnit.DAYS);
        response.sendRedirect(redirectURL + "?grant_token=" + grantToken);
    }

    @GetMapping("/sign_in")
    public String signInGet() {
        return "sign_in";
    }
    private static final String redirectTemplate = "redirect:/auth?client_id=%s&redirect_url=%s";
    @PostMapping("/sign_in")
    public String signInPost(
            @RequestParam("mail") String mail,
            @RequestParam("password") String password,
            @SessionAttribute("client_id") String clientID,
            @SessionAttribute("redirect_url") String redirectURL,
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
        return String.format(redirectTemplate, clientID, redirectURL);
    }
}
