package com.example.university.entity;

/**
 * User role type.
 */

public enum Role {
	ADMIN, USER;

	public String getName() {
		return name().toLowerCase();
	}

}
