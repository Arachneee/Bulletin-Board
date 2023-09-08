package com.arachneee.bulletinboard.web.form;

import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostAddForm {

	@NotEmpty
	private String title;

	@NotEmpty
	private String content;
}
