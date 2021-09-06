package com.example.university.entity;

/**
 * Applicant entity. This transfer object characterized by city, district,
 * school, foreign user id field and blocked state, which is false by default,
 * but may be changed by admin.
 */
public class Applicant extends Entity {

	private static final long serialVersionUID = 2565574420335652970L;
	private String city;
	private String district;
	private String school;
	private int userId;
	private boolean blockedStatus;

	public Applicant(String city, String district, String school, User user) {
		this(city, district, school, user.getId());
	}

	public Applicant(String city, String district, String school, int userId) {
		this.city = city;
		this.district = district;
		this.school = school;
		this.userId = userId;
		this.blockedStatus = false;
	}

	public Applicant() {
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public boolean getBlockedStatus() {
		return blockedStatus;
	}

	public void setBlockedStatus(boolean blockedStatus) {
		this.blockedStatus = blockedStatus;
	}

	@Override
	public String toString() {
		return "Applicant [city=" + city + ", district=" + district + ", school="
				+ school + ", userId=" + userId + ", isBlocked=" + blockedStatus
				+ "]";
	}

}
