package com.arachneee.bulletinboard.repository;


import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.web.search.CommentSearchCondition;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
	void save(Comment comment);

	Comment findById(Long id);

	void delete(Long id);

	Long findMemberIdByCommentId(Long commentId);

	String findContentById(Long commentId);

	List<Comment> findCommentsByPostId(Long postId, CommentSearchCondition commentSearchCondition, Integer commentPageSize);

    Long countByPostId(Long postId);

    List<Comment> findEmpathyComments(Long postId);
}
