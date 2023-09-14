package com.arachneee.bulletinboard.repository.comment;


import com.arachneee.bulletinboard.web.dto.CommentSearchCondition;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.repository.CommentRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

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

	@Override
	public List<Comment> findCommentsByPostId(Long postId, CommentSearchCondition commentSearchCondition, Integer commentPageSize) {

		String jpql = "select c from Comment c" +
				" join fetch c.member" +
				" where c.post.id = :postId" +
				" order by c.createTime " + getSortCode(commentSearchCondition);

		Integer commentPage = commentSearchCondition.getCommentPage();
		Integer skipPageSize = (commentPage - 1) * commentPageSize;

		return em.createQuery(jpql, Comment.class)
				.setParameter("postId", postId)
				.setFirstResult(skipPageSize)
				.setMaxResults(commentPageSize)
				.getResultList();

	}

	private static String getSortCode(CommentSearchCondition commentSearchCondition) {
		return commentSearchCondition.getCommentSortCode().equals("NEW") ? "desc" : "asc";
	}
}
