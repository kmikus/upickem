package com.upickem;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upickem.model.Login;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoginWebIntegrationTest {

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	private String url = "http://localhost:";
	
	@Test
	public void testLoginValid() throws JsonParseException, JsonMappingException, IOException {
		Login loginInfo = new Login("abc", "123");
		ResponseEntity<String> response = restTemplate.postForEntity(url + port + "/api/v1/login",
				loginInfo, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		System.out.println(response.getBody());
		
		ObjectMapper om = new ObjectMapper();
		JsonNode jn = om.readTree(response.getBody().toString());
		assertThat(jn.get("authenticated").asBoolean()).isTrue();
	}
}
