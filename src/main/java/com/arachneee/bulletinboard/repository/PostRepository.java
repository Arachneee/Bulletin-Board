package com.arachneee.bulletinboard.repository;

import java.util.List;
import java.util.Optional;

import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.web.dto.PostPreDto;
import com.arachneee.bulletinboard.web.form.SearchForm;

public interface PostRepository {
	Post save(Post post);

	Post findById(Long id);

	List<Post> findByName(String name);

	List<Post> findByLoginId(String loginId);

	List<Post> findAll();

	void update(Long id, Post post);
	void delete(Long id);

	List<PostPreDto> search(SearchForm searchForm);

	List<PostPreDto> findPostPreDtoAll();
}
