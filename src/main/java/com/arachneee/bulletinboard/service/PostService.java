package com.arachneee.bulletinboard.service;

import java.time.LocalDateTime;
import java.util.List;

import com.arachneee.bulletinboard.web.dto.PostPreDto;
import com.arachneee.bulletinboard.web.dto.PostViewDto;
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

	public List<PostPreDto> search(SearchForm searchForm) {
		return postRepository.search(searchForm);
	}

	public PostViewDto findPostViewDto(Long id) {
		return postRepository.findViewDtoById(id);
	}

	public PostViewDto view(Long id) {
		PostViewDto postViewDto = postRepository.findViewDtoById(id);
		postViewDto.setViewCount(postViewDto.getViewCount() + 1);
		postRepository.updateViewCount(id, postViewDto.getViewCount());
		return postViewDto;
	}

	public void update(Long id, PostAddForm postAddForm) {
		postRepository.update(id, postAddForm);
	}

	public void delete(Long id) {
		postRepository.delete(id);
	}

	public boolean isNotRightMember(Member member, Long id) {
		return !member.getId().equals(postRepository.findMemberIdByPostID(id));
	}




}
