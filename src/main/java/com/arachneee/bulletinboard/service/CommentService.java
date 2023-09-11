package com.arachneee.bulletinboard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.repository.CommentRepository;
import com.arachneee.bulletinboard.web.dto.CommentViewDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;

	public void save(Comment comment) {
		commentRepository.save(comment);
	}

	public List<CommentViewDto> findByPostId(Long postId) {
		return commentRepository.findByPostId(postId);
	}
}
