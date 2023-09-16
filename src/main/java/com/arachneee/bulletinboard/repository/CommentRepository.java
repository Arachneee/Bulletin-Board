package com.arachneee.bulletinboard.repository;


import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.web.dto.CommentSearchCondition;
import com.arachneee.bulletinboard.web.dto.CommentViewDto;

import java.util.List;

public interface CommentRepository {
	void save(Comment comment);

	Comment findById(Long id);

	void delete(Long id);

	Long findMemberIdByCommentId(Long commentId);

	String findContentById(Long commentId);

	List<Comment> findCommentsByPostId(Long postId, CommentSearchCondition commentSearchCondition, Integer commentPageSize);

    Long countByPostId(Long postId);
}
