package com.example.university.entities;

/**
 * Faculty entity. Every faculty is characterized by the name, amount of budget
 * and total places. The amount of budget places must always be less than amount
 * of total places for some faculty.
 */
public class Faculty extends Entity {

	private static final long serialVersionUID = 1590962657803610445L;
	private String nameRu;
	private String nameEn;
	private int budgetPlaces;
	private int totalPlaces;

	public Faculty(String nameRu, String nameEn, int budgetPlaces, int totalPlaces) {
		super();
		this.nameRu = nameRu;
		this.nameEn = nameEn;
		this.budgetPlaces = budgetPlaces;
		this.totalPlaces = totalPlaces;
	}

	public Faculty() {
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

	public int getBudgetPlaces() {
		return budgetPlaces;
	}

	public void setBudgetPlaces(int budgetPlaces) {
		this.budgetPlaces = budgetPlaces;
	}

	public int getTotalPlaces() {
		return totalPlaces;
	}

	public void setTotalPlaces(int totalPlaces) {
		this.totalPlaces = totalPlaces;
	}

	@Override
	public String toString() {
		return "Faculty [nameRu=" + nameRu + ", nameEn=" + nameEn
				+ ", budgetPlaces=" + budgetPlaces + ", totalPlaces=" + totalPlaces
				+ "]";
	}

}
