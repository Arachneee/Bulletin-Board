package com.arachneee.bulletinboard.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PostUpdateDto {

	@NotEmpty
	private String title;

	@NotEmpty
	private String content;
}
