package com.arachneee.bulletinboard.web.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;


@Getter @Setter
public class MemberAddForm {

	@NotBlank
	@Length(max=30)
	private String loginId;
	@NotBlank
	@Length(max=30)
	private String password;
	@NotBlank
	@Length(max=30)
	private String passwordRe;
	@NotBlank
	@Length(max=30)
	private String name;
}
