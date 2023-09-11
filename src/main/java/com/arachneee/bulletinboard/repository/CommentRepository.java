package com.arachneee.bulletinboard.repository;

import java.util.List;

import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.web.dto.CommentAddDto;
import com.arachneee.bulletinboard.web.dto.CommentViewDto;

public interface CommentRepository {
	void save(CommentAddDto commentAddDto);
	List<CommentViewDto> findByPostId(Long postId);

}
