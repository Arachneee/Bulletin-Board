package com.arachneee.bulletinboard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.PostRepository;

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
}
