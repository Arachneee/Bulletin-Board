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

	@Override
	public void update(Long id, String content) {
		Comment comment = em.find(Comment.class, id);
		comment.update(content);
	}

	@Override
	public void delete(Long id) {
		Comment comment = em.find(Comment.class, id);
		em.remove(comment);
	}

	public Comment findById(Long commentId) {
		return em.find(Comment.class, commentId);
	}

	@Override
	public String findContentById(Long commentId) {
		Comment comment = em.find(Comment.class, commentId);
		return comment.getContent();
	}

	@Override
	public Long findMemberIdByCommentId(Long commentId) {
		Comment comment = em.find(Comment.class, commentId);
		return comment.getMember().getId();
	}


}
