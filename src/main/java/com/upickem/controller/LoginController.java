package com.upickem.controller;

import com.jayway.jsonpath.Option;
import com.upickem.model.Login;
import com.upickem.repository.LoginRepository;

import java.util.Optional;

import javax.xml.ws.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/")
public class LoginController {

    @Autowired
    private LoginRepository loginRepository;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    ResponseEntity<Optional<Login>> login(@RequestBody Login loginInfo) {
        Login result = loginRepository.findById(loginInfo.getUsername());
//        return new ResponseEntity<Login>(loginInfo, HttpStatus.OK);
        return new ResponseEntity<Optional<Login>>(result, HttpStatus.OK);
    }

}
