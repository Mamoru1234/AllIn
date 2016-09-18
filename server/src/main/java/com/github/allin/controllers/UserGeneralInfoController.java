package com.github.allin.controllers;

import com.github.allin.models.GeneralInfo;
import com.github.allin.models.PublicDAO;
import com.github.allin.services.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 */
@Controller
public class UserGeneralInfoController {
    @Autowired
    PublicDAO<GeneralInfo> generalInfoPublicDAO;
    @Autowired
    AccessTokenService accessTokenService;

    @GetMapping("/user/general_info")
    public String getGeneralInfoPage(
            @SessionAttribute("user_id")String userID,
            Model model
    ) {
        GeneralInfo generalInfo;
        try {
            generalInfo = generalInfoPublicDAO.getByID(userID);
        } catch (EmptyResultDataAccessException e) {
            generalInfo = GeneralInfo.builder()
                .country("")
                .gender(GeneralInfo.Gender.UNDEF)
                .surName("")
                .firstName("")
                .build();
            System.out.println("--------------------------------------------------------");
            System.out.println(generalInfo.getGender().toString().length());
            generalInfoPublicDAO.insert(userID, generalInfo);
        }
        model.addAttribute("genders", GeneralInfo.Gender.values());
        model.addAttribute("generalInfo", generalInfo);
        return "general_info";
    }

    @PostMapping("/user/general_info")
    public String generalInfoForm(
            @RequestParam("f_name") String firstName,
            @RequestParam("s_name") String surName,
            @RequestParam("country") String country,
            @RequestParam("gender") String gender,
            @SessionAttribute("user_id") String userID
    ) {
        GeneralInfo generalInfo = GeneralInfo.builder()
                .firstName(firstName)
                .surName(surName)
                .country(country)
                .gender(GeneralInfo.Gender.valueOf(gender))
                .build();
        generalInfoPublicDAO.update(userID, generalInfo);
        return "welcome";
    }
    @RequestMapping("/api/{access_token}/general_info")
    @ResponseBody
    public GeneralInfo getGetInfo(
            @PathVariable("access_token") String accessToken
    ) {
        String userID = accessTokenService.getClientID(accessToken);
        return generalInfoPublicDAO.getByID(userID);
    }
}
