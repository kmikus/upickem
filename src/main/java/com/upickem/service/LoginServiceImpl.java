package com.upickem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upickem.model.Login;
import com.upickem.repository.LoginRepository;

@Service
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	private LoginRepository loginRepository;
	
	@SuppressWarnings("null")
	public boolean login(Login loginInfo) {
		Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	    Login resultFromDb = loginRepository.findOne(loginInfo.getUsername());
        if (resultFromDb != null || loginInfo.getPassword().equals(resultFromDb.getPassword())) {
        	logger.info("Authenticated user " + loginInfo.getUsername());
        	return true;
        }
        logger.warn("Invalid credentials for user " + loginInfo.getUsername());
	    return false;
	}
}
