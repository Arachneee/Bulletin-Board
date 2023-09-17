package com.arachneee.bulletinboard.domain;

import static jakarta.persistence.FetchType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Comment {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@Lob
	@Column(nullable = false)
	private String content;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	private LocalDateTime createTime;

	@OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
	private List<CommentEmpathy> commentEmpathies = new ArrayList<>();

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


	public void update(String content) {
		setContent(content);
	}

	public Integer getEmpathyCount() {
		return commentEmpathies.size();
	}

	public void addEmpathy(CommentEmpathy commentEmpathy) {
		commentEmpathies.add(commentEmpathy);
	}

	public boolean isAlreadyEmpathized(Long memberId) {
		return getMemberIds().contains(memberId);
	}

	private List<Long> getMemberIds() {
		return commentEmpathies.stream()
				.map(CommentEmpathy::getMemberId)
				.collect(Collectors.toList());
	}

	public boolean isWriter(Long memberId) {
		return member.getId().equals(memberId);
	}

	public int empathyCountDiff(final Comment other) {
		return this.getEmpathyCount() - other.getEmpathyCount();
	}

}
