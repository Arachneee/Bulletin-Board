package com.arachneee.bulletinboard.repository.post;

import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.PostRepository;
import com.arachneee.bulletinboard.web.dto.PostPreDto;
import com.arachneee.bulletinboard.web.search.PostSearchCondition;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class MemoryPostRepository implements PostRepository {

	private static final Map<Long, Post> postTable = new ConcurrentHashMap<>();
	private long sequence = 0L;

	public void updateViewCount(Long id, int viewCount) {
		Post findPost = postTable.get(id);
		findPost.view();
	}

	@Override
	public Post findWithMemberById(Long postId) {
		return findById(postId);
	}

	@Override
	public Post findById(Long id) {
		return postTable.get(id);
	}

	@Override
	public Long findMemberIdByPostID(Long id) {
		return postTable.get(id).getMember().getId();
	}

	@Override
	public void save(Post post) {
		post.setId(++sequence);
		postTable.put(post.getId(), post);
	}

	public List<Post> findAll() {
		return new ArrayList<>(postTable.values());
	}


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
	public List<PostPreDto> search(PostSearchCondition postSearchCondition, Long pageSize) {
		String searchString = postSearchCondition.getSearchString();
		String searchCode = postSearchCondition.getSearchCode();
		String sortCode = postSearchCondition.getSortCode();
		Long page = postSearchCondition.getPage();

		Long skipPageSize = (page - 1L) * pageSize;

		return findAll().stream()
			.filter(post -> isSearchCondition(searchString, searchCode, post))
			.sorted((post1, post2) -> comparePost(sortCode, post1, post2))
			.map(PostPreDto::new)
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

	@Override
	public Post findWithCommentsById(Long postId) {
		return findById(postId);
	}
}
