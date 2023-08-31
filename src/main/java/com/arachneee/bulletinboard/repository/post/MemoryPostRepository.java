package com.arachneee.bulletinboard.repository.post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.PostRepository;
import com.arachneee.bulletinboard.web.form.SearchForm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class MemoryPostRepository implements PostRepository {

	private static final Map<Long, Post> postTable = new ConcurrentHashMap<>();
	private static long sequence = 0L;

	@Override
	public Post save(Post post) {
		post.setId(++sequence);
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

	@Override
	public List<Post> search(SearchForm searchForm) {
		String searchString = searchForm.getSearchString();
		String searchCode = searchForm.getSearchCode();
		String sortCode = searchForm.getSortCode();

		return findAll().stream()
			.filter(post -> isSearchCondition(searchString, searchCode, post))
			.sorted((post1, post2) -> comparePost(sortCode, post1, post2))
			.collect(Collectors.toList());
	}

	private int comparePost(String sortCode, Post post1, Post post2) {
		if (sortCode.equals("OLD")) {
			return post2.getCreateTime().compareTo(post1.getCreateTime());
		}
		if (sortCode.equals("VIEW")) {
			return post2.getViewCount() - post1.getViewCount();
		}
		// sortCode.equals("NEW") // 조회순
		return post1.getCreateTime().compareTo(post2.getCreateTime());
	}

	private static boolean isSearchCondition(String searchString, String searchCode, Post post) {
		if (searchCode.equals("TITLE")) {
			return post.getTitle().contains(searchString);
		}
		if (searchCode.equals("CONTENT")) {
			return post.getContent().contains(searchString);
		}
		if (searchCode.equals("NAME")) {
			return post.getMember().getName().contains(searchString);
		}
		return true;
	}
}
