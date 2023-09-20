package com.arachneee.bulletinboard.repository.member;


import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Primary
public class JpaMemberRepository implements MemberRepository {

	private final EntityManager em;

	@Override
	public void save(Member member) {
		em.persist(member);
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
