package com.arachneee.bulletinboard.web.form;


import com.arachneee.bulletinboard.domain.Post;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PostEditForm {

	@NotBlank
	private String title;

	@NotBlank
	private String content;

	public PostEditForm() {
	}

	public PostEditForm(Post post) {
		title = post.getTitle();
		content = post.getContent();
	}
}