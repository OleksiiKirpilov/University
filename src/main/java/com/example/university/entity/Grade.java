package com.example.university.entity;

/**
 * Grade entity. Every instance is characterized by foreign keys from subject and
 * applicant, grade value. So every grade references to specific subject and
 * applicant that get it on some exam.
 */
public class Grade extends Entity {

	private static final long serialVersionUID = -6225323023971292703L;
	private int subjectId;
	private int applicantId;
	private int mark;
	private String examType;

	public Grade(int subjectId, int applicantId, int mark, String examType) {
		super();
		this.subjectId = subjectId;
		this.applicantId = applicantId;
		this.mark = mark;
		this.examType = examType;
	}

	public Grade() {
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public int getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(int applicantId) {
		this.applicantId = applicantId;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String markType) {
		this.examType = markType;
	}

	@Override
	public String toString() {
		return "Grade [subjectId=" + subjectId + ", applicantId=" + applicantId
				+ ", mark=" + mark + ", examType=" + examType + "]";
	}

}
