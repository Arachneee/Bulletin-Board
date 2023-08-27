package com.arachneee.bulletinboard.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Post {

	private Long id;
	private String title;
	private String content;
	private User user;
	private LocalDate createTime;
}
