package com.arachneee.bulletinboard.repository;

import java.util.List;

import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.web.dto.PostPreDto;
import com.arachneee.bulletinboard.web.dto.PostViewDto;


public interface PostRepository {
	void save(Post post);

	void update(Long id, String title, String content);
	void delete(Long id);

	List<PostPreDto> search(String searchCode, String searchString, String sortCode, Long page, Long pageSize);


	PostViewDto findViewDtoById(Long id);

	void updateViewCount(Long id, int viewCount);

	Long findMemberIdByPostID(Long id);


	Long countAll(String searchCode, String searchString);
}
