package com.example.university.entities;

import java.io.Serializable;

/**
 * Basic common parent for all entities. Provides id field and get/set methods
 * to it.
 */
public abstract class Entity implements Serializable {

	private static final long serialVersionUID = 1113613887250453747L;

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
