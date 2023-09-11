package com.arachneee.bulletinboard.web.dto;

import java.time.LocalDateTime;

import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.domain.Post;

import lombok.Getter;

@Getter
public class CommentAddDto {

	private String content;
	private Long postId;
	private Long memberId;
	private LocalDateTime createTime;

	protected CommentAddDto() {
	}

	public static CommentAddDto create(String content, Long postId, Long memberId) {
		CommentAddDto comment = new CommentAddDto();
		comment.setContent(content);
		comment.setPostId(postId);
		comment.setMemberId(memberId);
		comment.setCreateTime(LocalDateTime.now());

		return comment;
	}

	private void setContent(String content) {
		this.content = content;
	}

	private void setPostId(Long postId) {
		this.postId = postId;
	}

	private void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	private void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
}
