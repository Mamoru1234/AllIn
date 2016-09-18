package com.github.allin.controllers;

import com.github.allin.models.Client;
import com.github.allin.models.ClientDAO;
import com.github.allin.models.User;
import com.github.allin.models.UserDAO;
import com.github.allin.services.GrantTokenService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 */
@Log4j
@Controller
@SessionAttributes({"client_id"})
public class UserPermissionController {
    @Autowired
    ClientDAO clientDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    GrantTokenService grantTokenService;

    @GetMapping("/user/permission")
    public String getPermission(
        @SessionAttribute("client_id") String clientID,
        @SessionAttribute("user_id") String userID,
        Model model
    ) {
        User user = userDAO.getByID(userID);
        log.debug(user);
        model.addAttribute("user", user);
        Client client = clientDAO.getByID(clientID);
        log.debug(client);
        model.addAttribute("client", client);
        return "permission";
    }
    @PostMapping("/user/permission")
    public void postPermissionForm(
            @RequestParam("client_id") String clientID,
            @RequestParam("user_id") String userID,
            @SessionAttribute("redirect_url") String redirectURL,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String grantToken = grantTokenService.getToken(userID);
//        remove client_id from session
        request.getSession().removeAttribute("client_id");
        response.sendRedirect(redirectURL + "?grant_token="+grantToken);
    }
}
