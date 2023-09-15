package com.arachneee.bulletinboard.service;

import java.util.List;
import java.util.stream.Collectors;

import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.repository.CommentRepository;
import com.arachneee.bulletinboard.repository.post.JdbcPostRepository;
import com.arachneee.bulletinboard.web.dto.*;
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
	private final CommentRepository commentRepository;
	private final Long PAGE_SIZE = 10L;
	private final Integer COMMENT_PAGE_SIZE = 10;

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

	public PostViewDto viewAndFindPostViewDto(Long postId, Long memberId, CommentSearchCondition commentSearchCondition) {
		Post post = postRepository.findWithMemberById(postId);

		post.view();

		if (postRepository instanceof JdbcPostRepository) {
			postRepository.updateViewCount(postId, post.getViewCount());
		}

		PostViewDto postViewDto = PostViewDto.from(post);

		List<Comment> comments = commentRepository.findCommentsByPostId(postId, commentSearchCondition, COMMENT_PAGE_SIZE);

		postViewDto.setComments(comments.stream()
										.map(comment -> CommentViewDto.from(comment, memberId))
										.collect(Collectors.toList()));

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

	public boolean isLastCommentPage(Long postId, Integer commentPage) {
		return commentRepository.countByPostId(postId) <= commentPage * COMMENT_PAGE_SIZE;
	}
}
