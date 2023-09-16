package com.arachneee.bulletinboard.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

	private final MemberRepository memberRepository;

	public Member login(String loginId, String password) {
		return memberRepository.findByLoginId(loginId)
			.filter(member -> member.isRightPassword(password))
			.orElse(null);
	}
}
