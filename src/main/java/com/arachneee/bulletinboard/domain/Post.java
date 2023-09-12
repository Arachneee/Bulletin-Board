package com.arachneee.bulletinboard.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;

@Entity
@Getter
public class Post {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@Column(nullable = true, length = 100)
	private String title;

	@Column(nullable = true)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
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

	public static Post createRowMap(String title, String content, Member member, LocalDateTime localDateTime, Integer viewCount) {
		Post post = new Post();

		post.setTitle(title);
		post.setContent(content);
		post.setMember(member);
		post.setCreateTime(localDateTime);
		post.setViewCount(viewCount);

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
