package com.arachneee.bulletinboard.web.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentAddForm {

	@NotBlank
	private String commentContent;
}
