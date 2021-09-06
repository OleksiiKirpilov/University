package com.example.university.entity;

/**
 * Subject entity. Every subject is characterized by its name.
 */
public class Subject extends Entity {

	private static final long serialVersionUID = -5388561545513613948L;
	private String nameRu;
	private String nameEn;

	public Subject(String nameRu, String nameEn) {
		this.nameRu = nameRu;
		this.nameEn = nameEn;
	}

	public Subject() {
	}

	public String getNameRu() {
		return nameRu;
	}

	public void setNameRu(String nameRu) {
		this.nameRu = nameRu;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	@Override
	public String toString() {
		return "Subject [nameRu=" + nameRu + ", nameEng=" + nameEn + "]";
	}

}
