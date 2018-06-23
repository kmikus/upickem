package com.upickem.service;

import org.springframework.stereotype.Service;

import com.upickem.model.Login;

@Service
public interface LoginService {
	
	public boolean login(Login loginInfo);
	
}
