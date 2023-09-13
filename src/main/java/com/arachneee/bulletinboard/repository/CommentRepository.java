package com.arachneee.bulletinboard.repository;


import com.arachneee.bulletinboard.domain.Comment;

public interface CommentRepository {
	void save(Comment comment);

	void update(Long id, String content);
	void delete(Long id);

	Long findMemberIdByCommentId(Long commentId);

	String findContentById(Long commentId);
}
