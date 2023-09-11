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

	protected Comment() {
	}

	public static Comment create(String content, Post post, Member member) {
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setPost(post);
		comment.setMember(member);
		comment.setCreateTime(LocalDateTime.now());

		return comment;
	}

	private void setContent(String content) {
		this.content = content;
	}

	private void setPost(Post post) {
		this.post = post;
	}

	private void setMember(Member member) {
		this.member = member;
	}

	private void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
}
