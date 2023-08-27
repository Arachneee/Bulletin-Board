package com.arachneee.bulletinboard.domain.member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
	Member save(Member member);
	Member findById(Long id);
	Optional<Member> findByName(String name);
	List<Member> findAll();
}
