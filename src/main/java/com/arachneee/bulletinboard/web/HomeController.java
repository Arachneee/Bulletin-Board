package com.arachneee.bulletinboard.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.arachneee.bulletinboard.domain.member.Member;
import com.arachneee.bulletinboard.web.session.SessionConst;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
	@GetMapping("/")
	public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

		if (loginMember == null) {
			log.info("session login state false");
			return "home";
		}

		model.addAttribute("member", loginMember);
		log.info("session login state true");
		return "loginHome";
	}

	// @GetMapping("/")
	public String loginForm() {
		return "loginHome";
	}
}
