package com.arachneee.bulletinboard.web;

import org.springframework.stereotype.Component;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.MemberRepository;
import com.arachneee.bulletinboard.repository.PostRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestDataInit {

	private final MemberRepository memberRepository;
	private final PostRepository postRepository;

	@PostConstruct
	public void init() {
		Member member = new Member();
		member.setLoginId("aaa");
		member.setPassword("1234");
		member.setName("nameA");

		memberRepository.save(member);

		for (int i = 1; i <= 10; i++) {
			Post post = new Post();
			post.setTitle("test 제목 " + i);
			post.setContent("test 내용 " + i);
			post.setMember(member);

			postRepository.save(post);
		}

	}
}
