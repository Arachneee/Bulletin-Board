package com.arachneee.bulletinboard.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String addMemberForm(@ModelAttribute MemberAddForm memberAddForm) {
		return "members/addMemberForm";
	}

	@PostMapping("/add")
	public String saveMember(@Valid @ModelAttribute MemberAddForm memberAddForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors() || validateMemberAddForm(memberAddForm, bindingResult)) {
			return "members/addMemberForm";
		}

		memberService.save(memberAddForm.getLoginId(), memberAddForm.getPassword(), memberAddForm.getName());
		return "redirect:/";
	}

	private boolean validateMemberAddForm(MemberAddForm memberAddForm, BindingResult bindingResult) {
		if (isSamePassword(memberAddForm.getPassword(), memberAddForm.getPasswordRe())) {
			bindingResult.rejectValue("passwordRe","reject" ,"비밀번호가 일치하지 않습니다.");
		}

		if (memberService.isDuplicatedLoginId(memberAddForm.getLoginId())) {
			bindingResult.rejectValue("loginId","duplicated" ,"중복된 아이디입니다.");
		}

		if (memberService.isDuplicatedName(memberAddForm.getName())) {
			bindingResult.rejectValue("name","duplicated" ,"중복된 이름입니다.");
		}

		if (bindingResult.hasErrors()) {
			return true;
		}

		return false;
	}

	private static boolean isSamePassword(String password, String passwordRe) {
		return !password.equals(passwordRe);
	}


}
