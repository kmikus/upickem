/**
 * @author	Kevin Mikus
 */

package com.upickem.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upickem.model.Login;
import com.upickem.service.LoginService;

@RestController
@RequestMapping(value = "/api/v1/")
public class LoginController {

	/**
	 * 
	 */
    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<HashMap<String, Boolean>> login(@RequestBody Login loginInfo) {
        Boolean authenticated = loginService.login(loginInfo);
        HashMap<String, Boolean> authPair = new HashMap<String, Boolean>();
        authPair.put("authenticated", authenticated);
        return new ResponseEntity<HashMap<String,Boolean>>(authPair, HttpStatus.OK);
    }

}
