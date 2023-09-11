package com.arachneee.bulletinboard.domain;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class Comment {

	private Long id;
	private String content;
	private Post post;
	private Member member;
	private LocalDateTime createTime;
}
