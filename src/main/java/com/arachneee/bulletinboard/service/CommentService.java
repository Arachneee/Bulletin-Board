package com.arachneee.bulletinboard.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arachneee.bulletinboard.repository.CommentRepository;
import com.arachneee.bulletinboard.web.dto.CommentAddDto;
import com.arachneee.bulletinboard.web.dto.CommentViewDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

	private final CommentRepository commentRepository;


	public void save(String content, Long postId, Long memberId) {
		commentRepository.save(CommentAddDto.create(content, postId, memberId));
	}

	public List<CommentViewDto> findByPostId(Long postId) {
		return commentRepository.findByPostId(postId);
	}
}
