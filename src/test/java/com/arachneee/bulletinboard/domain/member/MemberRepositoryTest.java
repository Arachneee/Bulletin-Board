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

	@Test
	public void createAndFindByIdTest() {
		//given
		Member userA = new Member();
		userA.setName("nameA");
		userA.setLoginId("userA");
		userA.setPassword("1234");

		//when
		userRepository.save(userA);

		//then
		Assertions.assertThat(userRepository.findById(userA.getId())).isEqualTo(userA);
	}




}
