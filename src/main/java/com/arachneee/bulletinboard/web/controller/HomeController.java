package com.arachneee.bulletinboard.web.controller;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
	@GetMapping("/")
	public String home(@Login Member loginMember,
					   Model model) {

		if (loginMember == null) {
			return "loginHome";
		}

		model.addAttribute("member", loginMember);
		return "redirect:/posts";
	}
}
