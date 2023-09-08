package com.arachneee.bulletinboard.service;

import java.time.LocalDateTime;
import java.util.List;

import com.arachneee.bulletinboard.web.dto.PostPreDto;
import com.arachneee.bulletinboard.web.dto.PostViewDto;
import com.arachneee.bulletinboard.web.form.PostAddForm;
import com.arachneee.bulletinboard.web.form.PostSearchForm;
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

	public void save(String title, String content, Member member) {
		postRepository.save(Post.create(title, content, member, LocalDateTime.now(), 0));
	}

	public List<PostPreDto> search(String searchCode, String searchString, String sortCode) {
		return postRepository.search(searchCode, searchString, sortCode);
	}

	public PostViewDto findPostViewDto(Long id) {
		return postRepository.findViewDtoById(id);
	}

	public PostViewDto viewAndFindPostViewDto(Long id) {
		PostViewDto postViewDto = postRepository.findViewDtoById(id);

		postViewDto.view();

		postRepository.updateViewCount(id, postViewDto.getViewCount());

		return postViewDto;
	}

	public void update(Long id, String title, String content) {
		postRepository.update(id, title, content);
	}

	public void delete(Long id) {
		postRepository.delete(id);
	}

	public boolean isNotRightMember(Member member, Long id) {
		return member.getId().equals(postRepository.findMemberIdByPostID(id));
	}




}
