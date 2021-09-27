package com.example.university.utils;

/**
 * Path holder to jsp pages and controller commands
 */
public final class Path {

	public static final String WELCOME_PAGE = "welcome.jsp";
	public static final String ERROR_PAGE = "/WEB-INF/view/errorPage.jsp";

	public static final String FORWARD_USER_REGISTRATION_PAGE = "/WEB-INF/view/user/addUser.jsp";
	public static final String REDIRECT_USER_REGISTRATION_PAGE = "controller?command=userRegistration";

	public static final String FORWARD_ADMIN_REGISTRATION_PAGE = "/WEB-INF/view/admin/addAdmin.jsp";
	public static final String REDIRECT_ADMIN_REGISTRATION_PAGE = "controller?command=adminRegistration";

	public static final String REDIRECT_TO_PROFILE = "controller?command=viewProfile";
	public static final String REDIRECT_TO_FACULTY = "controller?command=viewFaculty&name_en=";
	public static final String REDIRECT_TO_SUBJECT = "controller?command=viewSubject&name_en=";
	public static final String REDIRECT_TO_VIEW_ALL_FACULTIES = "controller?command=viewAllFaculties";
	public static final String REDIRECT_TO_VIEW_ALL_SUBJECTS = "controller?command=viewAllSubjects";

	public static final String FORWARD_ADMIN_PROFILE = "/WEB-INF/view/admin/profile/view.jsp";
	public static final String FORWARD_ADMIN_PROFILE_EDIT = "/WEB-INF/view/admin/profile/edit.jsp";

	public static final String FORWARD_USER_PROFILE = "/WEB-INF/view/user/profile/view.jsp";
	public static final String FORWARD_USER_PROFILE_EDIT = "/WEB-INF/view/user/profile/edit.jsp";

	public static final String REDIRECT_EDIT_PROFILE = "controller?command=editProfile";

	public static final String FORWARD_FACULTY_VIEW_ALL_ADMIN = "/WEB-INF/view/admin/faculty/list.jsp";
	public static final String FORWARD_FACULTY_VIEW_ALL_USER = "/WEB-INF/view/user/faculty/list.jsp";

	public static final String FORWARD_FACULTY_VIEW_ADMIN = "/WEB-INF/view/admin/faculty/view.jsp";
	public static final String FORWARD_FACULTY_EDIT_ADMIN = "/WEB-INF/view/admin/faculty/edit.jsp";
	public static final String REDIRECT_FACULTY_EDIT_ADMIN = "controller?command=editFaculty&name_en=";
	public static final String FORWARD_FACULTY_ADD_ADMIN = "/WEB-INF/view/admin/faculty/add.jsp";
	public static final String REDIRECT_FACULTY_ADD_ADMIN = "controller?command=addFaculty";

	public static final String FORWARD_FACULTY_VIEW_USER = "/WEB-INF/view/user/faculty/view.jsp";
	public static final String FORWARD_FACULTY_APPLY_USER = "/WEB-INF/view/user/faculty/apply.jsp";

	public static final String FORWARD_SUBJECT_VIEW_ALL_ADMIN = "/WEB-INF/view/admin/subject/list.jsp";
	public static final String FORWARD_SUBJECT_VIEW_ADMIN = "/WEB-INF/view/admin/subject/view.jsp";
	public static final String FORWARD_SUBJECT_ADD_ADMIN = "/WEB-INF/view/admin/subject/add.jsp";
	public static final String REDIRECT_SUBJECT_ADD_ADMIN = "/WEB-INF/view/admin/subject/add.jsp";
	public static final String FORWARD_SUBJECT_EDIT_ADMIN = "/WEB-INF/view/admin/subject/edit.jsp";
	public static final String REDIRECT_SUBJECT_EDIT_ADMIN = "controller?command=editSubject&name_en=";

	public static final String FORWARD_APPLICANT_PROFILE = "/WEB-INF/view/admin/applicant/view.jsp";
	public static final String REDIRECT_APPLICANT_PROFILE = "controller?command=viewApplicant&userId=";

	public static final String FORWARD_REPORT_SHEET_VIEW = "/WEB-INF/view/admin/report/view.jsp";
	public static final String REDIRECT_REPORT_SHEET_VIEW = "controller?command=createReport&id=";

	private Path() {
	}
}