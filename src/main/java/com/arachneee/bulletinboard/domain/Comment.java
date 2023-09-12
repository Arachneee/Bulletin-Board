package com.arachneee.bulletinboard.domain;

import static jakarta.persistence.FetchType.*;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class Comment {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@Lob
	@Column(nullable = true)
	private String content;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "member_id")
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

	public Comment(Long id, String content, Post post, Member member, LocalDateTime createTime) {
		this.id = id;
		this.content = content;
		setPost(post);
		this.member = member;
		this.createTime = createTime;
	}

	private void setContent(String content) {
		this.content = content;
	}

	private void setPost(Post post) {
		this.post = post;
		post.getComments().add(this);
	}

	private void setMember(Member member) {
		this.member = member;
	}

	private void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
}
