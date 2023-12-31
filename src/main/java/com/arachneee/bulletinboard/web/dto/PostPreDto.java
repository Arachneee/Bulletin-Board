package com.arachneee.bulletinboard.web.dto;

import java.time.LocalDateTime;

import com.arachneee.bulletinboard.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
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

	public PostPreDto(Post post) {
		id = post.getId();
		title = post.getTitle();
		name = post.getMember().getName();
		createTime = post.getCreateTime();
		viewCount = post.getViewCount();
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
