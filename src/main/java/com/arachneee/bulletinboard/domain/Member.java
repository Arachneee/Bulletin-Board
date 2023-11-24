package com.arachneee.bulletinboard.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Entity
@Data
public class Member {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(nullable = false, length = 30)
	private String loginId;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, length = 30)
	private String name;

	private String role;

	protected Member() {
	}

	public static Member create(String loginId, String password, String name, String role) {
		Member member = new Member();
		member.setLoginId(loginId);
		member.setPassword(password);
		member.setName(name);
		member.setRole(role);

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

	private void setRole(String role) {
		this.role = role;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isRightPassword(String password) {
		return this.password.equals(password);
	}

	public boolean isSameName(String name) {
		return this.name.equals(name);
	}
}
