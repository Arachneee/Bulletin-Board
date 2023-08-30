package com.arachneee.bulletinboard.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.PostRepository;
import com.arachneee.bulletinboard.web.dto.PostUpdateDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;

	public void save(Post post) {
		postRepository.save(post);
	}

	public List<Post> findAll() {
		log.info("postService findAll 실행");
		return postRepository.findAll();
	}

	public Post findById(Long id) {
		return postRepository.findById(id);
	}

	public void update(Long id, PostUpdateDto postUpdateDto) {
		Post findPost = postRepository.findById(id);
		findPost.setCreateTime(LocalDateTime.now());
		findPost.setTitle(postUpdateDto.getTitle());
		findPost.setContent(postUpdateDto.getContent());
	}

	public boolean isNotRightMember(Member member, Long id) {
		return !member.getId().equals(postRepository.findById(id).getMember().getId());
	}

	public void delete(Long id) {
		postRepository.delete(id);
	}

	public Post view(Long id) {
		Post post = postRepository.findById(id);
		post.setViewCount(post.getViewCount() + 1);
		return post;
	}
}
