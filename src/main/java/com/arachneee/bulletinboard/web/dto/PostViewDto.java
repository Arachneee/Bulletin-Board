package com.arachneee.bulletinboard.web.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.arachneee.bulletinboard.domain.Post;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class PostViewDto {

	private Long id;
	private String title;
	private String content;
	private String name;
	private LocalDateTime createTime;
	private Integer viewCount;
	private List<CommentViewDto> comments;


	public static PostViewDto from(Post post) {
		PostViewDto postViewDto = new PostViewDto();

		postViewDto.setId(post.getId());
		postViewDto.setTitle(post.getTitle());
		postViewDto.setContent(post.getContent());
		postViewDto.setName(post.getMember().getName());
		postViewDto.setCreateTime(post.getCreateTime());
		postViewDto.setViewCount(post.getViewCount());

		return postViewDto;
	}
}
