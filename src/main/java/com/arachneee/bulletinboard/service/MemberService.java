package com.arachneee.bulletinboard.service;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

	private final MemberRepository memberRepository;

	public boolean isDuplicatedLoginId(String loginId) {
		return memberRepository.countLoginId(loginId) >= 1;
	}

	public boolean isDuplicatedName(String name) {
		return memberRepository.countName(name) >= 1;
	}

	public void save(String loginId, String password, String name, String role) {
		memberRepository.save(Member.create(loginId, password, name, role));
	}
}
