package com.upickem;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.upickem.model.Login;
import com.upickem.repository.LoginRepository;
import com.upickem.service.LoginServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceImplTest {
	
	@InjectMocks
	LoginServiceImpl loginServiceImpl;
	
	@Mock
	LoginRepository loginRepository;

	@Test
	public void testLoginValid() {
		Login inputLogin = new Login("abc", "123");
		when(loginRepository.findOne(inputLogin.getUsername())).thenReturn(new Login("abc", "123"));
		assertThat(loginServiceImpl.login(inputLogin)).isTrue();
	}
}
