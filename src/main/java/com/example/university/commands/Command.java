package com.example.university.commands;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.university.utils.RequestType;

/**
 * Main interface for the Command pattern implementation.
 */
public abstract class Command implements Serializable {

	private static final long serialVersionUID = 8879403039606311780L;

	protected static final String ERROR_FILL_ALL_FIELDS = "error_message.fill_all_fields";
	protected static final String ERROR_FACULTY_DEPENDS = "error_message.can_not_delete_faculty_dependent";
	protected static final String ERROR_CAN_NOT_FIND_USER = "error_message.can_not_find_user";
	protected static final String ERROR_EMAIL_USED = "error_message.email_in_use";
	protected static final String MESSAGE_ACCOUNT_CREATED = "profile.view_jsp.message.account_created";
	protected static final String ERROR_FACULTY_EXISTS = "error_message.faculty_exists";

	/**
	 * Execution method for command. Returns path to go to based on the user
	 * request. If Command is specific to some user role, then subclasses in
	 * this method should perform validation and grant or not permissions to
	 * proceed.
	 *
	 * @param request
	 *            - client request
	 * @param response
	 *            - server response
	 * @param requestType
	 *            - client HTTP method
	 * @return Address to go once the command is executed.
	 * @throws IOException
	 * @throws ServletException
	 * @see RequestType
	 */
	public abstract String execute(HttpServletRequest request,
			HttpServletResponse response, RequestType requestType)
			throws IOException, ServletException;


	protected void setErrorMessage(HttpServletRequest request, String messageKey) {
		setMessage(request, "errorMessage", messageKey);
	}

	protected void setOkMessage(HttpServletRequest request, String messageKey) {
		setMessage(request, "successfulMessage", messageKey);
	}

	protected void setMessage(HttpServletRequest request, String attribute, String messageKey) {
		HttpSession session = request.getSession();
		session.setAttribute(attribute, messageKey);
	}


	@Override
	public final String toString() {
		return getClass().getSimpleName();
	}
}