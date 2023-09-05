package com.arachneee.bulletinboard.web.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PostViewDto {

	private Long id;
	private String title;
	private String content;
	private String name;
	private LocalDateTime createTime;
	private Integer viewCount;
}
