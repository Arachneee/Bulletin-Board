package com.arachneee.bulletinboard.web.dto;

import java.time.LocalDateTime;


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

	public void view() {
		viewCount++;
	}
}
