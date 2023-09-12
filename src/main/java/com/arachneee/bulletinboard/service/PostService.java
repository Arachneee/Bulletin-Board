package com.arachneee.bulletinboard.service;

import java.util.List;

import com.arachneee.bulletinboard.web.dto.PostPreDto;
import com.arachneee.bulletinboard.web.dto.PostViewDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

	private final PostRepository postRepository;
	private final Long PAGE_SIZE = 10L;

	public void save(String title, String content, Member member) {
		postRepository.save(Post.create(title, content, member));
	}

	public List<PostPreDto> search(String searchCode, String searchString, String sortCode, Long page) {
		return postRepository.search(searchCode, searchString, sortCode, page, PAGE_SIZE);
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

	public boolean isNotRightMember(Long memberId, Long id) {
		return !memberId.equals(postRepository.findMemberIdByPostID(id));
	}


	public boolean isLastPage(String searchCode, String searchString, Long presentPage) {
		return postRepository.countAll(searchCode, searchString) <= presentPage * PAGE_SIZE;
	}
}
