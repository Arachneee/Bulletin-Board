package com.arachneee.bulletinboard.web.controller;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.service.LoginService;
import com.arachneee.bulletinboard.web.form.LoginForm;
import com.arachneee.bulletinboard.web.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {

//	private final LoginService loginService;

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
						@RequestParam(value = "exception", required = false) String exception, Model model) {
		model.addAttribute("error", error);
		model.addAttribute("exception", exception);

		return "login/loginForm";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		return "redirect:/login";
	}

	@GetMapping("/denied")
	public String accessDenied(@RequestParam(value = "exception", required = false) String exception, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Member member = (Member) authentication.getPrincipal();


		model.addAttribute("name", member.getName());
		model.addAttribute("role", member.getRole());
		model.addAttribute("memberName", member.getName());
		model.addAttribute("exception", exception);

		return "denied";
	}

//	@PostMapping("/login")
//	public String login(@Valid LoginForm loginForm, BindingResult bindingResult,
//						@RequestParam(defaultValue = "/") String redirectURL,
//						HttpServletRequest request) {
//		if (bindingResult.hasErrors()) {
//			return "login/loginForm";
//		}
//
//		Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
//
//		if (member == null) {
//			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
//			return "login/loginForm";
//		}
//
//		request.getSession()
//				.setAttribute(SessionConst.LOGIN_MEMBER, member);
//
//		return "redirect:" + redirectURL;
//	}
//
//	@PostMapping("/logout")
//	public String logout(HttpServletRequest request) {
//		HttpSession session = request.getSession(false);
//		if (session != null) {
//			session.invalidate();
//		}
//
//		return "redirect:/";
//	}
}
