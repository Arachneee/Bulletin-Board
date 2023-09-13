package com.arachneee.bulletinboard.repository.comment;


import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.repository.CommentRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Primary
public class JpaCommentRepository implements CommentRepository {

	private final EntityManager em;

	@Override
	public void save(Comment comment) {
		em.persist(comment);
	}
}
