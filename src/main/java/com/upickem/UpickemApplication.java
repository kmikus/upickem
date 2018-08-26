package com.upickem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UpickemApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpickemApplication.class, args);
	}
}
