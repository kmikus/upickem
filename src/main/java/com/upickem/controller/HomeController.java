package com.upickem.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class HomeController {

	@RequestMapping(value="/")
	RedirectView redirectToIndex() {
		return new RedirectView("/index.html");
	}
}
