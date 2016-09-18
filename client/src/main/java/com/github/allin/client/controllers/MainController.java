package com.github.allin.client.controllers;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 */
@Controller
public class MainController {
    private static String registerURLTemplate = "http://localhost:7080/oauth/authorize?client_id=%s&redirect_url=%s";
    @GetMapping("/")
    public String index(
        Model model
    ) {
        model.addAttribute("registerURL", String.format(registerURLTemplate, "client_id", "http://localhost:3000/handler"));
        return "main";
    }
    @GetMapping("/handler")
    public String handler(
            @RequestParam("grant_token") String grantToken,
            Model model
    ) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://localhost:7080/oauth/token");

        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("client_id", "client_id"));
        nameValuePairs.add(new BasicNameValuePair("client_secret", "secret"));
        nameValuePairs.add(new BasicNameValuePair("grant_token", grantToken));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
            httpclient.close();
        }
        model.addAttribute("registerURL", "");
        return "main";
    }
}
