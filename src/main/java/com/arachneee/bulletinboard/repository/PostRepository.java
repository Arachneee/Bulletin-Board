package com.arachneee.bulletinboard.repository;

import java.util.List;
import java.util.Optional;

import com.arachneee.bulletinboard.domain.Post;

public interface PostRepository {
	Post save(Post post);

	Post findById(Long id);

	List<Post> findByName(String name);

	List<Post> findByLoginId(String loginId);

	List<Post> findAll();

	void update(Long id, Post post);
	void delete(Long id);
}