package com.arachneee.bulletinboard.service;

import java.util.List;
import java.util.stream.Collectors;

import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.repository.CommentRepository;
import com.arachneee.bulletinboard.web.dto.*;
import com.arachneee.bulletinboard.web.dto.PostEditDto;
import com.arachneee.bulletinboard.web.search.CommentSearchCondition;
import com.arachneee.bulletinboard.web.search.PostSearchCondition;
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
@Transactional(readOnly = true)
public class PostService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final Long PAGE_SIZE = 10L;
	private final Integer COMMENT_PAGE_SIZE = 10;

	@Transactional
	public void save(String title, String content, Member member) {
		postRepository.save(Post.create(title, content, member));
	}

	public List<PostPreDto> search(PostSearchCondition postSearchCondition) {
		return postRepository.search(postSearchCondition, PAGE_SIZE);
	}

	public PostEditDto findPostEditDto(Long id) {
		Post post = postRepository.findById(id);
		return PostEditDto.from(post);
	}

	@Transactional
	public void view(Long postId) {
		Post post = postRepository.findById(postId);
		post.view();
	}

	public PostViewDto findPostViewDto(Long postId, Long memberId, CommentSearchCondition commentSearchCondition) {
		Post post = postRepository.findWithMemberById(postId);
		List<Comment> comments = commentRepository.findCommentsByPostId(postId, commentSearchCondition, COMMENT_PAGE_SIZE);

		PostViewDto postViewDto = PostViewDto.from(post);

		postViewDto.setComments(comments.stream()
										.map(comment -> CommentViewDto.from(comment, memberId))
										.collect(Collectors.toList()));

		return postViewDto;
	}

	@Transactional
	public void update(Long id, String title, String content) {
		Post post = postRepository.findById(id);
		post.update(title, content);
	}

	@Transactional
	public void delete(Long id) {
		postRepository.delete(id);
	}

	public boolean isNotRightMember(Long memberId, Long id) {
		return !memberId.equals(postRepository.findMemberIdByPostID(id));
	}

	public boolean isLastPage(PostSearchCondition postSearchCondition, Long presentPage) {
		return postRepository.countAll(postSearchCondition.getSearchCode(), postSearchCondition.getSearchString()) <= presentPage * PAGE_SIZE;
	}

	public boolean isLastCommentPage(Long postId, Integer commentPage) {
		return commentRepository.countByPostId(postId) <= ((long) commentPage * COMMENT_PAGE_SIZE);
	}
}
