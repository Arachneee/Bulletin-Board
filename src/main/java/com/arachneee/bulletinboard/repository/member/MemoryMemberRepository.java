package com.arachneee.bulletinboard.repository.member;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.repository.MemberRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryMemberRepository implements MemberRepository {

	private static final Map<Long, Member> memberTable = new ConcurrentHashMap<>();
	private static long sequence = 0L;

	public void save(Member member) {
		member.setId(++sequence);
		memberTable.put(member.getId(), member);
	}

	public Member findById(Long id) {
		return memberTable.get(id);
	}

	@Override
	public Optional<Member> findByLoginId(String loginId) {
		return findAll().stream()
			.filter(member -> member.getLoginId().equals(loginId))
			.findAny();
	}

	@Override
	public List<Member> findAll() {
		return new ArrayList<>(memberTable.values());
	}


	public void clear() {
		memberTable.clear();
	}

	@Override
	public Long countLoginId(String loginId) {
		return findAll().stream()
			.filter(member -> member.getLoginId().equals(loginId))
			.count();
	}

	@Override
	public Long countName(String name) {
		return findAll().stream()
			.filter(member -> member.getName().equals(name))
			.count();
	}
}
