package com.arachneee.bulletinboard.domain.member;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class MemoryMemberRepository implements MemberRepository {

	private static final Map<Long, Member> memberTable = new ConcurrentHashMap<>();
	private static long sequence = 0L;

	public Member save(Member member) {
		member.setId(++sequence);
		memberTable.put(member.getId(), member);
		return member;
	}

	public Member findById(Long id) {
		return memberTable.get(id);
	}

	@Override
	public Optional<Member> findByName(String name) {
		return findAll().stream()
			.filter(member -> member.getName().equals(name))
			.findAny();
	}

	@Override
	public List<Member> findAll() {
		return new ArrayList<>(memberTable.values());
	}

	public void clear() {
		memberTable.clear();
	}
}
