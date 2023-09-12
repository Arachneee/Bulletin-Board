package com.arachneee.bulletinboard.repository;


import com.arachneee.bulletinboard.domain.Comment;

public interface CommentRepository {
	void save(Comment comment);
}
