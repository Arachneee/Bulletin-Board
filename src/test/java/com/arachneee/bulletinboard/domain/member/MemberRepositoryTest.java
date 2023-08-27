package com.arachneee.bulletinboard.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.arachneee.bulletinboard.domain.member.Member;
import com.arachneee.bulletinboard.domain.member.MemberRepository;

public class MemberRepositoryTest {

	@Test
	public void createAndFindByIdTest() {
		//given
		Member userA = new Member();
		userA.setId(1L);
		userA.setName("nameA");
		userA.setLoginId("userA");
		userA.setPassword("1234");

		MemberRepository userRepository = new MemberRepository();

		//when
		userRepository.save(userA);

		//then
		Assertions.assertThat(userRepository.findById(userA.getId())).isEqualTo(userA);
	}
}
