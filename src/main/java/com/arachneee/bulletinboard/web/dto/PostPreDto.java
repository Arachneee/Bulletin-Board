package com.arachneee.bulletinboard.web.dto;

import java.time.LocalDateTime;

import com.arachneee.bulletinboard.domain.Post;
import lombok.Getter;

@Getter
public class PostPreDto {

	private Long id;
	private String title;
	private String name;
	private LocalDateTime createTime;
	private Integer viewCount;

	public static PostPreDto from(Post post) {
		PostPreDto postPreDto = new PostPreDto();

		postPreDto.setId(post.getId());
		postPreDto.setCreateTime(post.getCreateTime());
		postPreDto.setTitle(post.getTitle());
		postPreDto.setViewCount(post.getViewCount());
		postPreDto.setName(post.getMember().getName());

		return postPreDto;
	}

	public static PostPreDto create(Long id, String title, String name, LocalDateTime createTime, Integer viewCount) {
		PostPreDto postPreDto = new PostPreDto();

		postPreDto.setId(id);
		postPreDto.setTitle(title);
		postPreDto.setName(name);
		postPreDto.setCreateTime(createTime);
		postPreDto.setViewCount(viewCount);

		return postPreDto;
	}

	private void setId(Long id) {
		this.id = id;
	}

	private void setTitle(String title) {
		this.title = title;
	}

	private void setName(String name) {
		this.name = name;
	}

	private void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	private void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}
}
