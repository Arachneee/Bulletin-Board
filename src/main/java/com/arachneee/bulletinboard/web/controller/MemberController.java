package com.arachneee.bulletinboard.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.service.MemberService;
import com.arachneee.bulletinboard.web.form.MemberAddForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/add")
	public String addMember(@ModelAttribute MemberAddForm memberAddForm) {
		return "members/addMemberForm";
	}

	@PostMapping("/add")
	public String saveMember(@Valid @ModelAttribute MemberAddForm memberAddForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "members/addMemberForm";
		}

		if (!memberAddForm.getPassword().equals(memberAddForm.getPasswordRe())) {
			bindingResult.rejectValue("passwordRe","reject" ,"비밀번호가 일치하지 않습니다.");
			return "members/addMemberForm";
		}

		if (memberService.isDuplicatedLoginId(memberAddForm)) {
			bindingResult.rejectValue("loginId","duplicated" ,"중복된 아이디입니다.");
			return "members/addMemberForm";
		}

		if (memberService.isDuplicatedName(memberAddForm)) {
			bindingResult.rejectValue("name","duplicated" ,"중복된 이름입니다.");
			return "members/addMemberForm";
		}

		memberService.save(memberAddForm);
		return "redirect:/";
	}


}
