package com.arachneee.bulletinboard.domain;

import lombok.Data;

@Data
public class User {

	private Long id;
	private String loginId;
	private String password;
	private String name;
}
