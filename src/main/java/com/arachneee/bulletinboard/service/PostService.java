package com.arachneee.bulletinboard.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.arachneee.bulletinboard.web.dto.PostPreDto;
import com.arachneee.bulletinboard.web.form.PostAddForm;
import com.arachneee.bulletinboard.web.form.SearchForm;
import org.springframework.stereotype.Service;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;

	public void save(PostAddForm postAddForm, Member member) {
		Post post = new Post();

		post.setTitle(postAddForm.getTitle());
		post.setContent(postAddForm.getContent());
		post.setMember(member);
		post.setCreateTime(LocalDateTime.now());
		post.setViewCount(0);

		postRepository.save(post);
	}

	public List<Post> findAll() {
		log.info("postService findAll 실행");
		return postRepository.findAll();
	}

	public Post findById(Long id) {
		return postRepository.findById(id);
	}

	public void update(Long id, PostAddForm postAddForm) {
		Post findPost = postRepository.findById(id);
		findPost.setCreateTime(LocalDateTime.now());
		findPost.setTitle(postAddForm.getTitle());
		findPost.setContent(postAddForm.getContent());
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

	public List<PostPreDto> search(SearchForm searchForm) {
		if (searchForm.getSearchCode() == null && searchForm.getSearchString() == null && searchForm.getSortCode() == null) {
			return postRepository.findPostPreDtoAll();
		}
		return postRepository.search(searchForm);
	}
	// public List<PostPreDto> findPostPreDtoAll() {
	// 	log.info("postService findPostPreDtoAll 실행");
	// 	return postRepository.findPostPreDtoAll();
	// }
}
