package com.arachneee.bulletinboard.web;

import org.springframework.stereotype.Component;

import com.arachneee.bulletinboard.domain.member.Member;
import com.arachneee.bulletinboard.domain.member.MemberRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestDataInit {

	private final MemberRepository memberRepository;

	@PostConstruct
	public void init() {
		Member member = new Member();
		member.setLoginId("aaa");
		member.setPassword("1234");
		member.setName("nameA");

		memberRepository.save(member);
	}
}
