package com.example.university.entity;

/**
 * Faculty applicants entity. This is a compound entity class, that tells which
 * applicant applied for which faculty by referencing to their foreign keys.
 */
public class FacultyApplicants extends Entity {

	private static final long serialVersionUID = 1099698953477481899L;

	private int facultyId;
	private int applicantId;

	public FacultyApplicants(int facultyId, int applicantId) {
		this.facultyId = facultyId;
		this.applicantId = applicantId;
	}

	public FacultyApplicants(Faculty f, Applicant a) {
		this(f.getId(), a.getId());
	}

	public FacultyApplicants() {
	}

	public int getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(int facultyId) {
		this.facultyId = facultyId;
	}

	public int getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(int applicantId) {
		this.applicantId = applicantId;
	}

	@Override
	public String toString() {
		return "FacultyApplicants [facultyId=" + facultyId + ", applicantId="
				+ applicantId + "]";
	}

}
