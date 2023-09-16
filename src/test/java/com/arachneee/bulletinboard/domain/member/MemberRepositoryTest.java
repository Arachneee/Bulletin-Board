package com.arachneee.bulletinboard.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.repository.MemberRepository;
import com.arachneee.bulletinboard.repository.member.MemoryMemberRepository;

public class MemberRepositoryTest {
	MemberRepository userRepository = new MemoryMemberRepository();

	@AfterEach
	void afterEach() {
		if (userRepository instanceof  MemoryMemberRepository) {
			((MemoryMemberRepository)userRepository).clear();
		}
	}

}
