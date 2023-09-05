package com.arachneee.bulletinboard.service;

import org.springframework.stereotype.Service;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;

	public boolean isDuplicatedLoginId(Member member) {
		return memberRepository.countLoginId(member.getLoginId()) >= 1;
	}

	public boolean isDuplicatedName(Member member) {
		return memberRepository.countName(member.getName()) >= 1;
	}

	public void save(Member member) {
		memberRepository.save(member);
	}
}
