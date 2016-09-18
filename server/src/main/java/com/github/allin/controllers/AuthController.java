package com.github.allin.controllers;

import com.github.allin.models.Client;
import com.github.allin.models.ClientDAO;
import com.github.allin.services.AccessTokenService;
import com.github.allin.services.GrantTokenService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 */
@Log4j
@Controller
@SessionAttributes({"client_id", "redirect_url"})
public class AuthController {
    @Autowired
    ClientDAO clientDAO;

    @Autowired
    AccessTokenService accessTokenService;

    @Autowired
    GrantTokenService grantTokenService;

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

    @PostMapping("/oauth/token")
    @ResponseBody
    public Map<String, String> accessToken(
            @RequestParam("client_id") String clientID,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("grant_token") String grantToken
    ) {
        Client client;
        try {
            client = clientDAO.getByID(clientID);
        }catch (EmptyResultDataAccessException e) {
            log.debug("wrong id");
            throw new WrongInputException();
        }
        if (!client.getClientSecret().equals(clientSecret)) {
            log.debug("wrong secret");
            throw new WrongInputException();
        }
        String userID = grantTokenService.getUserID(grantToken);
        if (userID == null) {
            throw new WrongTokenException();
        }
        grantTokenService.invalidate(grantToken);
        Map<String, String> response = new HashMap<>();
        String accessToken = accessTokenService.getAccessToken(client, userID);
        response.put("access_token", accessToken);
        return response;
    }
    @ExceptionHandler(WrongInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> wrongInput() {
        Map<String, String> response = new HashMap<>();
        response.put("reason", "Wrong client id or secret");
        return response;
    }
    @ExceptionHandler(WrongTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> wrongToken() {
        Map<String, String> response = new HashMap<>();
        response.put("reason", "Wrong grant_token");
        return response;
    }
    private class WrongInputException extends RuntimeException {}
    private class WrongTokenException extends RuntimeException {}
}
