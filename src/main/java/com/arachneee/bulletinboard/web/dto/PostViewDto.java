package com.arachneee.bulletinboard.web.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.arachneee.bulletinboard.domain.Post;

import lombok.Data;



@Data
public class PostViewDto {

	private Long id;
	private String title;
	private String content;
	private String name;
	private LocalDateTime createTime;
	private Integer viewCount;
	private List<CommentViewDto> comments;


	public PostViewDto(Post post) {
		id = post.getId();
		title = post.getTitle();
		content = post.getContent();
		name = post.getMember().getName();
		createTime = post.getCreateTime();
		viewCount = post.getViewCount();
	}
}
