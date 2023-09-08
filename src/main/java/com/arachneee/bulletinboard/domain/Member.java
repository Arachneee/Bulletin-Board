package com.arachneee.bulletinboard.domain;


import lombok.Getter;

@Getter
public class Member {

	private Long id;


	private String loginId;

	private String password;

	private String name;

	protected Member() {
	}

	public static Member create(String loginId, String password, String name) {
		Member member = new Member();
		member.setLoginId(loginId);
		member.setPassword(password);
		member.setName(name);

		return member;
	}

	private void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	private void setPassword(String password) {
		this.password = password;
	}

	private void setName(String name) {
		this.name = name;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
