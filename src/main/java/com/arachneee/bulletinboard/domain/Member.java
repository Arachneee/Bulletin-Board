package com.arachneee.bulletinboard.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Member {

	private Long id;

	@NotEmpty
	private String loginId;
	@NotEmpty
	private String password;
	@NotEmpty
	private String name;
}
