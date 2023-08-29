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
		return memberRepository.findByLoginId(member.getLoginId()).isPresent();
	}

	public boolean isDuplicatedName(Member member) {
		return memberRepository.findByName(member.getName()).isPresent();
	}

	public void save(Member member) {
		memberRepository.save(member);
	}
}
