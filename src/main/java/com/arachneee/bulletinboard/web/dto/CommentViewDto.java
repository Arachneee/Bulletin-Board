package com.arachneee.bulletinboard.web.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CommentViewDto {
	private String content;
	private String name;
	private LocalDateTime createTime;

	protected CommentViewDto() {
	}

	public static CommentViewDto create(String content, String name, LocalDateTime createTime) {
		CommentViewDto commentViewDto = new CommentViewDto();
		commentViewDto.setContent(content);
		commentViewDto.setName(name);
		commentViewDto.setCreateTime(createTime);

		return commentViewDto;
	}

	private void setContent(String content) {
		this.content = content;
	}

	private void setName(String name) {
		this.name = name;
	}

	private void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
}
