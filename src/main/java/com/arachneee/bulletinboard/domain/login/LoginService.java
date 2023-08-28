package com.arachneee.bulletinboard.domain.login;

import org.springframework.stereotype.Service;

import com.arachneee.bulletinboard.domain.member.Member;
import com.arachneee.bulletinboard.domain.member.MemberRepository;

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
