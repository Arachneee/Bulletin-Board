package com.arachneee.bulletinboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.arachneee.bulletinboard.domain.member.Member;
import com.arachneee.bulletinboard.form.LoginForm;
import com.arachneee.bulletinboard.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;

	@GetMapping("/login")
	public String loginForm(@ModelAttribute LoginForm loginForm) {
		return "login/loginForm";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute LoginForm loginForm, HttpServletRequest request) {
		log.info("로그인 시도");
		Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

		if (member == null) {
			log.info("로그인 실패");
			return "login/loginForm";
		}

		HttpSession session = request.getSession();
		session.setAttribute("loginMember", member);
		log.info("로그인 성공");
		return "redirect:/";
	}


}
