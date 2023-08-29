package com.arachneee.bulletinboard.domain;

import java.time.LocalDate;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Post {

	private Long id;
	@NotEmpty
	private String title;
	@NotEmpty
	private String content;
	private Member member;
	private LocalDate createTime;
}
