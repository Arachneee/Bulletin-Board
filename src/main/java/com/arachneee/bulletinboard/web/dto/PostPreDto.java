package com.arachneee.bulletinboard.web.dto;

import java.time.LocalDateTime;

import com.arachneee.bulletinboard.domain.Member;

import lombok.Data;

@Data
public class PostPreDto {

	private Long id;
	private String title;
	private String name;
	private LocalDateTime createTime;
	private Integer viewCount;
}
