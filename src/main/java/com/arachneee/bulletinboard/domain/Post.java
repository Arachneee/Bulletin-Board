package com.arachneee.bulletinboard.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Post {

	private Long id;
	private String title;
	private String content;
	private Member member;
	private LocalDateTime createTime;
	private Integer viewCount;
}
