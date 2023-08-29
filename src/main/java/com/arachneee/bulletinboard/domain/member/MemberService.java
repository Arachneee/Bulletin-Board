package com.arachneee.bulletinboard.domain.member;

import org.springframework.stereotype.Service;

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
