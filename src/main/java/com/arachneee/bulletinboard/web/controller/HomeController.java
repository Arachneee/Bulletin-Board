package com.arachneee.bulletinboard.web.controller;

import com.arachneee.bulletinboard.web.argumentresolver.Login;
import com.arachneee.bulletinboard.web.dto.CommentSearchCondition;
import com.arachneee.bulletinboard.web.dto.PostSearchCondition;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.arachneee.bulletinboard.domain.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
	@GetMapping("/")
	public String home(@Login Member loginMember,
					   Model model) {

		if (loginMember == null) {
			log.info("session login state false");
			return "loginHome";
		}

		model.addAttribute("member", loginMember);
		log.info("session login state true");
		return "redirect:/posts";
	}
}
