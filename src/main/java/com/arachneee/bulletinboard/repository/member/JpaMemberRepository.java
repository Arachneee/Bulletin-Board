package com.arachneee.bulletinboard.repository.member;


import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.repository.MemberRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Primary
@Slf4j
@RequiredArgsConstructor
@Repository
public class JpaMemberRepository implements MemberRepository {

	@PersistenceContext
	private final EntityManager em;

	@Override
	public void save(Member member) {
		log.info("JpaMemberRepository save 실행 시작");
		em.persist(member);
		log.info("JpaMemberRepository save 실행 종료");
	}

	@Override
	public Member findById(Long id) {
		return em.find(Member.class, id);
	}

	@Override
	public Optional<Member> findByLoginId(String loginId) {
		String jpql = "select m from Member m where m.loginId = :loginId";

		return Optional.of(em.createQuery(jpql, Member.class)
			.setParameter("loginId", loginId)
			.getSingleResult());
	}

	@Override
	public List<Member> findAll() {
		String jpql = "select m from Member m";

		return em.createQuery(jpql, Member.class)
			.getResultList();
	}

	@Override
	public Long countLoginId(String loginId) {
		String jpql = "select count(m) from Member m where m.loginId = :loginId";

		return em.createQuery(jpql, Long.class)
			.setParameter("loginId", loginId)
			.getSingleResult();
	}

	@Override
	public Long countName(String name) {
		String jpql = "select count(m) from Member m where m.name = :name";

		return em.createQuery(jpql, Long.class)
			.setParameter("name", name)
			.getSingleResult();
	}
}
