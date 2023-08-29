package com.arachneee.bulletinboard.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.service.LoginService;
import com.arachneee.bulletinboard.web.form.LoginForm;
import com.arachneee.bulletinboard.web.session.SessionConst;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String login(@Valid @ModelAttribute LoginForm loginForm, BindingResult bindingResult,
						@RequestParam(defaultValue = "/") String redirectURL,
						HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			return "login/loginForm";
		}

		log.info("로그인 시도");
		Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

		if (member == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			log.info("로그인 실패");
			return "login/loginForm";
		}

		HttpSession session = request.getSession();
		session.setAttribute(SessionConst.LOGIN_MEMBER, member);

		log.info("로그인 성공");
		return "redirect:" + redirectURL;
	}

	@PostMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		return "redirect:/";
	}


}
