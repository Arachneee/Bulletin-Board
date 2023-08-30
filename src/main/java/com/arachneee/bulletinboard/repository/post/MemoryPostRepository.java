package com.arachneee.bulletinboard.repository.post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.PostRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class MemoryPostRepository implements PostRepository {

	private static final Map<Long, Post> postTable = new ConcurrentHashMap<>();
	private static long sequence = 0L;

	@Override
	public Post save(Post post) {
		post.setId(++sequence);
		post.setCreateTime(LocalDateTime.now());
		post.setViewCount(0);
		postTable.put(post.getId(), post);
		log.info("post save={}",post.getId());
		return post;
	}

	@Override
	public Post findById(Long id) {
		return postTable.get(id);
	}

	@Override
	public List<Post> findByName(String name) {
		return findAll().stream()
			.filter(post -> post.getMember().getName().equals(name))
			.collect(Collectors.toList());
	}

	@Override
	public List<Post> findByLoginId(String loginId) {
		return findAll().stream()
			.filter(post -> post.getMember().getLoginId().equals(loginId))
			.collect(Collectors.toList());
	}

	@Override
	public List<Post> findAll() {
		log.info("MemoryPostRepository findAll 현재 총 수량={}",postTable.size());
		return new ArrayList<>(postTable.values());
	}

	@Override
	public void update(Long id, Post post) {
		Post findPost = postTable.get(id);

		findPost.setContent(post.getContent());
		findPost.setTitle(post.getTitle());
	}

	@Override
	public void delete(Long id) {
		postTable.remove(id);
	}

	public void clear() {
		postTable.clear();
	}
}
