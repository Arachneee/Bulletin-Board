package com.arachneee.bulletinboard.repository;

import java.util.List;
import java.util.Optional;

import com.arachneee.bulletinboard.domain.Member;

public interface MemberRepository {
	Member save(Member member);
	Member findById(Long id);
	Optional<Member> findByName(String name);
	Optional<Member> findByLoginId(String loginId);
	List<Member> findAll();
}
