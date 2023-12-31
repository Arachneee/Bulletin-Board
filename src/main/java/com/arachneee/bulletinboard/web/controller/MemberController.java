package com.arachneee.bulletinboard.web.controller;

import com.arachneee.bulletinboard.service.MemberService;
import com.arachneee.bulletinboard.web.form.MemberAddForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;

	@GetMapping("/add")
	public String addMemberForm(MemberAddForm memberAddForm) {
		return "members/addMemberForm";
	}

	@PostMapping("/add")
	public String createMember(@Valid MemberAddForm memberAddForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors() || validateMemberAddForm(memberAddForm, bindingResult)) {
			return "members/addMemberForm";
		}

		String encodedPassword = passwordEncoder.encode( memberAddForm.getPassword());

		memberService.save(memberAddForm.getLoginId(),encodedPassword, memberAddForm.getName(), memberAddForm.getRole());
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

	private boolean isSamePassword(String password, String passwordRe) {
		return !password.equals(passwordRe);
	}


}
