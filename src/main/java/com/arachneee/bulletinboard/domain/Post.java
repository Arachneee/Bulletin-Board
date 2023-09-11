package com.arachneee.bulletinboard.domain;

import java.time.LocalDateTime;


import lombok.Getter;

@Getter
public class Post {

	private Long id;
	private String title;
	private String content;
	private Member member;
	private LocalDateTime createTime;
	private Integer viewCount;
	protected Post() {
	}
	public static Post create(String title, String content, Member member) {
		Post post = new Post();

		post.setTitle(title);
		post.setContent(content);
		post.setMember(member);
		post.setCreateTime(LocalDateTime.now());
		post.setViewCount(0);

		return post;
	}

	private void setTitle(String title) {
		this.title = title;
	}

	private void setContent(String content) {
		this.content = content;
	}

	private void setMember(Member member) {
		this.member = member;
	}

	private void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	private void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public void view() {
		viewCount++;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
