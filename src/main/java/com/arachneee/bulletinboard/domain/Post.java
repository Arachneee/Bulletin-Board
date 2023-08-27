package com.arachneee.bulletinboard.domain;

import java.time.LocalDate;

import com.arachneee.bulletinboard.domain.member.Member;

import lombok.Data;

@Data
public class Post {

	private Long id;
	private String title;
	private String content;
	private Member member;
	private LocalDate createTime;
}
