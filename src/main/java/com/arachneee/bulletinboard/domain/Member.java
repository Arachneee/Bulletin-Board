package com.arachneee.bulletinboard.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Member {

	private Long id;


	private String loginId;

	private String password;

	private String name;
}
