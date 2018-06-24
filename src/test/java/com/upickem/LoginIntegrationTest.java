package com.upickem;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.upickem.model.Login;
import com.upickem.service.LoginService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginIntegrationTest {

	@Autowired
	private LoginService loginService;
	
	@Test
	public void testLoginValid() {
		Login loginInfo = new Login("abc", "123");
		assertThat(loginService.login(loginInfo)).isTrue();
	}
}
