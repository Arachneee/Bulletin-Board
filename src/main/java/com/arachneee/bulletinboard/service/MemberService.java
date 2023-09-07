package com.arachneee.bulletinboard.service;

import org.springframework.stereotype.Service;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.repository.MemberRepository;
import com.arachneee.bulletinboard.web.form.MemberAddForm;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;

	public boolean isDuplicatedLoginId(MemberAddForm memberAddForm) {
		return memberRepository.countLoginId(memberAddForm.getLoginId()) >= 1;
	}

	public boolean isDuplicatedName(MemberAddForm memberAddForm) {
		return memberRepository.countName(memberAddForm.getName()) >= 1;
	}

	public void save(MemberAddForm memberAddForm) {
		Member member = new Member();
		member.setLoginId(memberAddForm.getLoginId());
		member.setPassword(memberAddForm.getPassword());
		member.setName(memberAddForm.getName());

		memberRepository.save(member);
	}
}
