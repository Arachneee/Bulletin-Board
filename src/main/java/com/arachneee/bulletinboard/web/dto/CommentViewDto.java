package com.arachneee.bulletinboard.web.dto;

import java.time.LocalDateTime;

import com.arachneee.bulletinboard.domain.Comment;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentViewDto {

	private Long id;
	private String content;
	private String name;
	private LocalDateTime createTime;
	private Integer empathyCount;
	private boolean commentShow;
	private boolean isEmpathy;

	public static CommentViewDto from(Comment comment, Long memberId) {
		CommentViewDto commentViewDto = new CommentViewDto();
		
		commentViewDto.setId(comment.getId());
		commentViewDto.setContent(comment.getContent());
		commentViewDto.setName(comment.getMember().getName());
		commentViewDto.setCreateTime(comment.getCreateTime());
		commentViewDto.setCommentShow(comment.isWriter(memberId));
		commentViewDto.setEmpathyCount(comment.getEmpathyCount());
		commentViewDto.setEmpathy(comment.isAlreadyEmpathized(memberId) || comment.isWriter(memberId));

		return commentViewDto;
	}

}
