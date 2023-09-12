package com.arachneee.bulletinboard.repository.post;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.PostRepository;
import com.arachneee.bulletinboard.web.dto.PostEditDto;
import com.arachneee.bulletinboard.web.dto.PostPreDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class JpaPostRepository implements PostRepository {

	@PersistenceContext
	private final EntityManager em;

	@Override
	public void save(Post post) {
		em.persist(post);
	}

	@Override
	public Post findById(Long id) {
		String jpql = "select p from Post p join fetch p.comments where p.id = :id";

		return em.createQuery(jpql, Post.class)
					.setParameter("id", id)
					.getSingleResult();
	}

	@Override
	public void update(Long id, String title, String content) {
		Post post = em.find(Post.class, id);
		post.update(title, content);
	}

	@Override
	public void delete(Long id) {
		Post post = em.find(Post.class, id);
		em.remove(post);
	}

	@Override
	public List<PostPreDto> search(String searchCode, String searchString, String sortCode, Long page, Long pageSize) {
		Integer skipPageSize = (page.intValue() - 1) * pageSize.intValue();

		String jpql = "select new com.arachneee.bulletinboard.web.dto.PostPreDto(p.id, p.title, m.name, p.createTime, p.viewCount)" +
						" from Post p" +
						" join p.member m" +
						" where :searchCode like :searchString" +
						" order by " + getSortSql(sortCode);

		return em.createQuery(jpql, PostPreDto.class)
			.setParameter("searchCode", getSearchSql(searchCode))
			.setParameter("searchString", "%" + searchString + "%")
			.setFirstResult(skipPageSize)
			.setMaxResults(pageSize.intValue())
			.getResultList();
	}

	private static String getSearchSql(String searchCode) {
		if (searchCode.equals("CONTENT")) {
			return "p.content";
		} else if (searchCode.equals("NAME")) {
			return "m.name";
		} else { // searchCode.equals("TITLE")
			return "p.title";
		}
	}

	private static String getSortSql(String sortCode) {
		if (sortCode.equals("NEW")) {
			return "p.createTime desc";
		} else if (sortCode.equals("VIEW")) {
			return "p.viewCount desc";
		} else { // sortCode.equals("OLD")
			return "p.createTime asc";
		}
	}

	@Override
	public PostEditDto findPostEditDtoById(Long id) {
		String jpql = "select new com.arachneee.bulletinboard.web.dto.PostViewDto(p.id, p.title, p.content, m.name, p.createTime, p.viewCount)" +
						" from Post p" +
						" join p.member m" +
						" where p.id = :id";

		return em.createQuery(jpql, PostEditDto.class)
			.setParameter("id", id)
			.getSingleResult();
	}

	@Override
	public void updateViewCount(Long id, int viewCount) {
		Post post = em.find(Post.class, id);
		post.view();
	}

	@Override
	public Long findMemberIdByPostID(Long id) {
		String jpql = "select m.id" +
			" from Post p" +
			" join p.member m" +
			" where p.id = :id";

		return em.createQuery(jpql, Long.class)
			.setParameter("id", id)
			.getSingleResult();
	}

	@Override
	public Long countAll(String searchCode, String searchString) {
		String jpql = "select count(p)" +
						" from Post p" +
						" join p.member m" +
						" where " + getSearchSql(searchCode) + " like '%" + searchString + "%'";

		return em.createQuery(jpql, Long.class)
			.getSingleResult();
	}

	@Override
	public List<Comment> findCommentsByPostId(Long id) {
		// String jpql = "select c from Comment c join fetch c.post where c.post.id = :id";
		String jpql = "select c from Comment c where c.post.id = :id";

		return em.createQuery(jpql, Comment.class)
			.setParameter("id", id)
			.getResultList();
	}
}
