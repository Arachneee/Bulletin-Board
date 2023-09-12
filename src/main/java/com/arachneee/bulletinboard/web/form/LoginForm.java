package com.arachneee.bulletinboard.web.form;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginForm {
	@NotBlank
	private String loginId;

	@NotBlank
	private String password;
}
