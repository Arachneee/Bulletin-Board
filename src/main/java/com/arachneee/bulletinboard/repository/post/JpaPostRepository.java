package com.arachneee.bulletinboard.repository.post;

import java.util.List;

import com.arachneee.bulletinboard.web.dto.PostSearchCondition;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.PostRepository;
import com.arachneee.bulletinboard.web.dto.PostPreDto;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class JpaPostRepository implements PostRepository {

	private final EntityManager em;

	@Override
	public void save(Post post) {
		em.persist(post);
	}

	@Override
	public Post findById(Long id) {
		return em.find(Post.class, id);
	}


	@Override
	public void delete(Long id) {
		Post post = em.find(Post.class, id);
		em.remove(post);
	}

	@Override
	public List<PostPreDto> search(PostSearchCondition postSearchCondition, Long pageSize) {

		String searchString = postSearchCondition.getSearchString();
		String searchCode = postSearchCondition.getSearchCode();
		String sortCode = postSearchCondition.getSortCode();
		Long page = postSearchCondition.getPage();

		Integer skipPageSize = (page.intValue() - 1) * pageSize.intValue();

		String jpql = "select new com.arachneee.bulletinboard.web.dto.PostPreDto(p.id, p.title, m.name, p.createTime, p.viewCount)" +
						" from Post p" +
						" join p.member m" +
						" where " + getSearchSql(searchCode) + " like '%" + searchString + "%'" +
						" order by " + getSortSql(sortCode);

		return em.createQuery(jpql, PostPreDto.class)
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
	public Post findWithCommentsById(Long postId) {
		String jpql = "select p from Post p" +
						" left join fetch p.comments c" +
						" left join fetch c.member m" +
						" where p.id = :postId";

		return em.createQuery(jpql, Post.class)
			.setParameter("postId", postId)
			.getSingleResult();
	}

	@Override
	public Post findWithMemberById(Long postId) {
		String jpql = "select p from Post p" +
						" left join fetch p.member m" +
						" where p.id = :postId";

		return em.createQuery(jpql, Post.class)
				.setParameter("postId", postId)
				.getSingleResult();
	}
}
