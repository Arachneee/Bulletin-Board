package com.arachneee.bulletinboard.repository;

import java.util.List;

import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.web.dto.PostPreDto;
import com.arachneee.bulletinboard.web.dto.PostViewDto;
import com.arachneee.bulletinboard.web.form.PostAddForm;
import com.arachneee.bulletinboard.web.form.PostSearchForm;

public interface PostRepository {
	Post save(Post post);

	void update(Long id, PostAddForm postAddForm);
	void delete(Long id);

	List<PostPreDto> search(PostSearchForm postSearchForm);


	PostViewDto findViewDtoById(Long id);

	void updateViewCount(Long id, int viewCount);

	Long findMemberIdByPostID(Long id);


}
