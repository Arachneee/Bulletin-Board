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

	public boolean isDuplicatedLoginId(String loginId) {
		return memberRepository.countLoginId(loginId) >= 1;
	}

	public boolean isDuplicatedName(String name) {
		return memberRepository.countName(name) >= 1;
	}

	public void save(String loginId, String password, String name) {
		memberRepository.save(Member.create(loginId, password, name));
	}
}
