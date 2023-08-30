package com.arachneee.bulletinboard.service;

import org.springframework.stereotype.Service;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {

	private final MemberRepository memberRepository;

	public Member login(String loginId, String password) {
		return memberRepository.findByLoginId(loginId)
			.filter(member -> member.getPassword().equals(password))
			.orElse(null);
	}

}