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

	public PostPreDto(Long id, String title, String name, LocalDateTime createTime, Integer viewCount) {
		this.id = id;
		this.title = title;
		this.name = name;
		this.createTime = createTime;
		this.viewCount = viewCount;
	}

	public static PostPreDto from(Post post) {
		return new PostPreDto(post.getId(), post.getTitle(), post.getMember().getName(), post.getCreateTime(), post.getViewCount());
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
