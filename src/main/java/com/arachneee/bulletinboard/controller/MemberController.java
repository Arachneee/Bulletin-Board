package com.arachneee.bulletinboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.arachneee.bulletinboard.domain.member.Member;
import com.arachneee.bulletinboard.domain.member.MemberRepository;
import com.arachneee.bulletinboard.form.LoginForm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

	private final MemberRepository memberRepository;

	@GetMapping("/add")
	public String addMember(@ModelAttribute Member member) {
		return "members/addMemberForm";
	}

	@PostMapping("/add")
	public String save(@ModelAttribute Member member) {
		memberRepository.save(member);
		return "redirect:/";
	}
}
