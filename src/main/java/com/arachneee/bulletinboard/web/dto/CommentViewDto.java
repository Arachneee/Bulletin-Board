package com.arachneee.bulletinboard.web.dto;

import java.time.LocalDateTime;

import com.arachneee.bulletinboard.domain.Comment;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentViewDto {
	private String content;
	private String name;
	private LocalDateTime createTime;

	public static CommentViewDto from(Comment comment) {
		CommentViewDto commentViewDto = new CommentViewDto();
		commentViewDto.setContent(comment.getContent());
		commentViewDto.setName(comment.getMember().getName());
		commentViewDto.setCreateTime(comment.getCreateTime());

		return commentViewDto;
	}
}
