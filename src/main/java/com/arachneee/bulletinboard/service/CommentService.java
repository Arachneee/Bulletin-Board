package com.arachneee.bulletinboard.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.CommentRepository;
import com.arachneee.bulletinboard.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;

	public void save(String content, Long postId, Member member) {
		Post post = postRepository.findById(postId);
		commentRepository.save(Comment.create(content, post, member));
	}

	public void update(Long commentId, String content) {
		commentRepository.update(commentId, content);
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
}
