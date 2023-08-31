package com.arachneee.bulletinboard.web.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PostAddForm {

	@NotEmpty
	private String title;

	@NotEmpty
	private String content;
}
