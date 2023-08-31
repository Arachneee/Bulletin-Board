package com.arachneee.bulletinboard.web;

import org.springframework.stereotype.Component;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.MemberRepository;
import com.arachneee.bulletinboard.repository.PostRepository;
import com.arachneee.bulletinboard.repository.member.MemoryMemberRepository;
import com.arachneee.bulletinboard.service.PostService;
import com.arachneee.bulletinboard.web.form.PostAddForm;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestDataInit {

	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final PostService postService;

	@PostConstruct
	public void init() {
		if (memberRepository instanceof MemoryMemberRepository) {
			Member member = new Member();
			member.setLoginId("aaa");
			member.setPassword("1234");
			member.setName("nameA");

			memberRepository.save(member);

			for (int i = 1; i <= 10; i++) {
				PostAddForm postAddForm = new PostAddForm();
				postAddForm.setTitle("test 제목 " + i);
				postAddForm.setContent("test 내용 " + i);

				postService.save(postAddForm, member);
			}
		}
	}
}
