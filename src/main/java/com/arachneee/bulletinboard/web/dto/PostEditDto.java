package com.arachneee.bulletinboard.web.dto;


import com.arachneee.bulletinboard.domain.Post;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class PostEditDto {

	private Long id;

	@NotBlank
	private String title;

	@NotBlank
	private String content;

	public PostEditDto(Long id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}

	public PostEditDto(Post post) {
		id = post.getId();
		title = post.getTitle();
		content = post.getContent();
	}

}
