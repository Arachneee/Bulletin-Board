package com.arachneee.bulletinboard.web.dto;

import java.time.LocalDateTime;

import com.arachneee.bulletinboard.domain.Post;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class PostViewDto {

	private Long id;

	@NotBlank
	private String title;

	@NotBlank
	private String content;
	private String name;
	private LocalDateTime createTime;
	private Integer viewCount;

	public PostViewDto(Long id, String title, String content, String name, LocalDateTime createTime,
		Integer viewCount) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.name = name;
		this.createTime = createTime;
		this.viewCount = viewCount;
	}

	public static PostViewDto from(Post post) {
		return new PostViewDto(post.getId(), post.getTitle(), post.getContent(), post.getMember().getName(), post.getCreateTime(), post.getViewCount());
	}
}
