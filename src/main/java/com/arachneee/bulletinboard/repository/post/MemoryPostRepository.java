package com.arachneee.bulletinboard.repository.post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.PostRepository;
import com.arachneee.bulletinboard.web.dto.PostPreDto;
import com.arachneee.bulletinboard.web.dto.PostViewDto;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class MemoryPostRepository implements PostRepository {

	private static final Map<Long, Post> postTable = new ConcurrentHashMap<>();
	private long sequence = 0L;

	@Override
	public PostViewDto findViewDtoById(Long id) {
		Post post = postTable.get(id);


		PostViewDto postViewDto = new PostViewDto();
		postViewDto.setId(post.getId());
		postViewDto.setTitle(post.getTitle());
		postViewDto.setContent(post.getContent());
		postViewDto.setName(post.getMember().getName());
		postViewDto.setCreateTime(post.getCreateTime());
		postViewDto.setViewCount(post.getViewCount());

		return postViewDto;
	}

	@Override
	public void updateViewCount(Long id, int viewCount) {
		Post findPost = postTable.get(id);
		findPost.view();
	}

	@Override
	public Long findMemberIdByPostID(Long id) {
		return postTable.get(id).getMember().getId();
	}

	@Override
	public void save(Post post) {
		post.setId(++sequence);
		postTable.put(post.getId(), post);
		log.info("post save={}",post.getId());
	}

	public List<Post> findAll() {
		log.info("MemoryPostRepository findAll 현재 총 수량={}",postTable.size());
		return new ArrayList<>(postTable.values());
	}

	@Override
	public void update(Long id, String title, String content) {
		Post findPost = postTable.get(id);
		findPost.update(title, content);

	}

	@Override
	public void delete(Long id) {
		postTable.remove(id);
	}

	public void clear() {
		postTable.clear();
	}

	@Override
	public List<PostPreDto> search(String searchCode, String searchString, String sortCode, Long page, Long pageSize) {
		log.info("searchString = {} searchCode = {} sortCode = {}", searchString, searchCode, sortCode);

		Long skipPageSize = (page - 1L) * pageSize;

		return findAll().stream()
			.filter(post -> isSearchCondition(searchString, searchCode, post))
			.sorted((post1, post2) -> comparePost(sortCode, post1, post2))
			.map(post -> PostPreDto.from(post))
				.skip(skipPageSize)
				.limit(pageSize)
			.collect(Collectors.toList());

	}

	private int comparePost(String sortCode, Post post1, Post post2) {
		if (sortCode.equals("OLD")) {
			return post1.getId().compareTo(post2.getId());
		}
		if (sortCode.equals("VIEW")) {
			return post2.getViewCount() - post1.getViewCount();
		}
		// sortCode.equals("NEW") // 조회순
		return post2.getCreateTime().compareTo(post1.getCreateTime());
	}

	private static boolean isSearchCondition(String searchString, String searchCode, Post post) {
		if (searchCode.equals("CONTENT")) {
			return post.getContent().contains(searchString);
		}
		if (searchCode.equals("NAME")) {
			return post.getMember().getName().contains(searchString);
		}
		// searchCode.equals("TITLE") // 제목
		return post.getTitle().contains(searchString);
	}

	@Override
	public Long countAll(String searchCode, String searchString) {
		return Long.valueOf(findAll().stream()
									 .filter(post -> isSearchCondition(searchString, searchCode, post))
				 					 .count());
	}
}
