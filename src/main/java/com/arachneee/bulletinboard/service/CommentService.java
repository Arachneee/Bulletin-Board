package com.arachneee.bulletinboard.service;


import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.domain.CommentEmpathy;
import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.CommentEmpathyRepository;
import com.arachneee.bulletinboard.repository.CommentRepository;
import com.arachneee.bulletinboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final CommentEmpathyRepository commentEmpathyRepository;

	public void save(String content, Long postId, Member member) {
		Post post = postRepository.findById(postId);
		commentRepository.save(Comment.create(content, post, member));
	}

	public void update(Long commentId, String content) {
		Comment comment = commentRepository.findById(commentId);
		comment.update(content);
	}

	public boolean isNotRightMember(Long memberId, Long commentId) {
		return !memberId.equals(commentRepository.findMemberIdByCommentId(commentId));
	}

	public String findContentById(Long commentId) {
		return commentRepository.findContentById(commentId);
	}

	public void delete(Long commentId) {
		commentRepository.delete(commentId);
	}

	public void empathy(Long commentId, Member member) {
		Comment comment = commentRepository.findById(commentId);

		if (comment.isAlreadyEmpathized(member.getId()) || comment.isWriter(member.getId())) {
			return;
		}

		CommentEmpathy commentEmpathy = CommentEmpathy.create(comment, member);
		commentEmpathyRepository.save(commentEmpathy);
	}

	public Optional<Comment> getBestComment(Long postId) {
		List<Comment> empathyComments = commentRepository.findEmpathyComments(postId);

		return empathyComments.stream()
				.max(Comment::empathyCountDiff);
	}
}
