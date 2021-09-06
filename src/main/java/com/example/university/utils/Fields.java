package com.example.university.utils;

/**
 * Utility class for field names in database tables.
 */
public final class Fields {

	public static final String ENTITY_ID = "id";
	public static final String GENERATED_KEY = "GENERATED_KEY";

	public static final String USER_EMAIL = "email";
	public static final String USER_PASSWORD = "password";
	public static final String USER_FIRST_NAME = "first_name";
	public static final String USER_LAST_NAME = "last_name";
	public static final String USER_ROLE = "role";
	public static final String USER_LANG = "lang";
	public static final String USER_ACTIVE_STATUS = "isActive";
	public static final String USER_FOREIGN_KEY_ID = "users_id";

	public static final String APPLICANT_CITY = "city";
	public static final String APPLICANT_DISTRICT = "district";
	public static final String APPLICANT_SCHOOL = "school";
	public static final String APPLICANT_IS_BLOCKED = "isBlocked";
	public static final String APPLICANT_ID = "applicant_id";

	public static final String FACULTY_NAME_RU = "name_ru";
	public static final String FACULTY_NAME_EN = "name_en";
	public static final String FACULTY_BUDGET_PLACES = "budget_places";
	public static final String FACULTY_TOTAL_PLACES = "total_places";
	public static final String FACULTY_ID = "faculty_id";

	public static final String SUBJECT_NAME_RU = "name_ru";
	public static final String SUBJECT_NAME_EN = "name_en";
	public static final String SUBJECT_ID = "subjects_id";

	public static final String MARK_VALUE = "value";
	public static final String MARK_EXAM_TYPE = "exam_type";
	public static final String MARK_FOREIGN_KEY_ID = "Mark_idMark";

	public static final String REPORT_SHEET_FACULTY_ID = "facultyId";
	public static final String REPORT_SHEET_USER_FIRST_NAME = "first_name";
	public static final String REPORT_SHEET_USER_LAST_NAME = "last_name";
	public static final String REPORT_SHEET_USER_EMAIL = "email";
	public static final String REPORT_SHEET_APPLICANT_IS_BLOCKED = "isBlocked";
	public static final String REPORT_SHEET_APPLICANT_PRELIMINARY_SUM = "preliminary_sum";
	public static final String REPORT_SHEET_APPLICANT_DIPLOMA_SUM = "diploma_sum";
	public static final String REPORT_SHEET_APPLICANT_TOTAL_SUM = "total_sum";

	private Fields() {}

}