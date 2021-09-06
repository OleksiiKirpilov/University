package com.example.university.commands.faculty;

import com.example.university.commands.Command;
import com.example.university.entity.Applicant;
import com.example.university.db.ApplicantDao;
import com.example.university.entity.User;
import com.example.university.db.UserDao;
import com.example.university.utils.Fields;
import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * View profile command.
 */
public class ViewApplicant extends Command {

	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = LogManager.getLogger(ViewApplicant.class);

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, RequestType requestType)
			throws IOException, ServletException {
		LOG.debug("Command execution");
		if (requestType == RequestType.GET) {
			return doGet(request, response);
		}
		return doPost(request, response);
	}

	/**
	 * Forwards admin to applicant profile.
	 *
	 * @return path to applicant profile page
	 */
	private String doGet(HttpServletRequest request,
			HttpServletResponse response) {

		int userId = Integer.parseInt(request.getParameter("userId"));
		UserDao userDao = new UserDao();
		// should not be null !
		User user = userDao.find(userId);
		request.setAttribute("first_name", user.getFirstName());
		LOG.trace("Set the request attribute: 'first_name' = "
				+ user.getFirstName());
		request.setAttribute("last_name", user.getLastName());
		LOG.trace("Set the request attribute: 'last_name' = "
				+ user.getLastName());
		request.setAttribute("email", user.getEmail());
		LOG.trace("Set the request attribute: 'email' = " + user.getEmail());
		request.setAttribute("role", user.getRole());
		LOG.trace("Set the request attribute: 'role' = " + user.getRole());

		ApplicantDao applicantDao = new ApplicantDao();
		// should not be null !!
		Applicant applicant = applicantDao.find(user);

		request.setAttribute(Fields.ENTITY_ID, applicant.getId());
		LOG.trace("Set the request attribute: 'id' = " + applicant.getId());
		request.setAttribute(Fields.APPLICANT_CITY, applicant.getCity());
		LOG.trace("Set the request attribute: 'city' = " + applicant.getCity());
		request.setAttribute(Fields.APPLICANT_DISTRICT, applicant.getDistrict());
		LOG.trace("Set the request attribute: 'district' = "
				+ applicant.getDistrict());
		request.setAttribute(Fields.APPLICANT_SCHOOL, applicant.getSchool());
		LOG.trace("Set the request attribute: 'school' = "
				+ applicant.getSchool());

		request.setAttribute(Fields.APPLICANT_IS_BLOCKED,
				applicant.getBlockedStatus());
		LOG.trace("Set the request attribute: 'isBlocked' = "
				+ applicant.getBlockedStatus());
		return Path.FORWARD_APPLICANT_PROFILE;
	}

	/**
	 * Changes blocked status of applicant after submitting button in applicant view
	 *
	 * @return redirects to view applicant page
	 */
	private String doPost(HttpServletRequest request,
			HttpServletResponse response) {
		int applicantId = Integer.parseInt(request.getParameter(Fields.ENTITY_ID));
		ApplicantDao applicantDao = new ApplicantDao();
		Applicant applicant = applicantDao.find(applicantId);
		boolean updatedBlockedStatus = !applicant.getBlockedStatus();
		applicant.setBlockedStatus(updatedBlockedStatus);
		LOG.trace("Applicant with 'id' = {} and changed 'isBlocked' status = {}"
				+ " record will be updated.", applicantId, updatedBlockedStatus);
		applicantDao.update(applicant);
		return Path.REDIRECT_APPLICANT_PROFILE + applicant.getUserId();
	}

}