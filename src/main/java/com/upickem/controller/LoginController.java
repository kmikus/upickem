package com.upickem.controller;

import com.upickem.model.Login;
import com.upickem.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/")
public class LoginController {

    @Autowired
    private LoginRepository loginRepository;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    Login login(@RequestBody Login loginInfo) {
        System.out.println("Here");
        return loginInfo;
    }

}
