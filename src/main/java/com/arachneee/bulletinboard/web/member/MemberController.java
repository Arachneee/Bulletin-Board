package com.arachneee.bulletinboard.web.member;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.arachneee.bulletinboard.domain.member.Member;
import com.arachneee.bulletinboard.domain.member.MemberRepository;

import jakarta.validation.Valid;
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
	public String save(@Valid @ModelAttribute Member member, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "members/addMemberForm";
		}

		if (isDuplicatedLoginId(member)) {
			bindingResult.rejectValue("loginId","duplicated" ,"중복된 아이디입니다");
			return "members/addMemberForm";
		}

		if (isDuplicatedName(member)) {
			bindingResult.rejectValue("name","duplicated" ,"중복된 이름입니다");
			return "members/addMemberForm";
		}

		memberRepository.save(member);
		return "redirect:/";
	}

	private boolean isDuplicatedLoginId(Member member) {
		return memberRepository.findByLoginId(member.getLoginId()).isPresent();
	}

	private boolean isDuplicatedName(Member member) {
		return memberRepository.findByName(member.getName()).isPresent();
	}
}
