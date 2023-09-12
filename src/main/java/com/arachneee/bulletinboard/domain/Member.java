package com.arachneee.bulletinboard.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Member {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(nullable = true, length = 30)
	private String loginId;

	@Column(nullable = true, length = 30)
	private String password;

	@Column(nullable = true, length = 30)
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
