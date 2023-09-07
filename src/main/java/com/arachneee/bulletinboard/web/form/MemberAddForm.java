package com.arachneee.bulletinboard.web.form;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberAddForm {

	@NotEmpty
	@Length(max=30)
	private String loginId;
	@NotEmpty
	@Length(max=30)
	private String password;
	@NotEmpty
	@Length(max=30)
	private String passwordRe;
	@NotEmpty
	@Length(max=30)
	private String name;
}
