package com.example.university.entities;

/**
 * User role type.
 */

public enum Role {
	ADMIN, USER;

	public String getName() {
		return name().toLowerCase();
	}

}
