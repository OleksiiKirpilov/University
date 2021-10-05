package com.example.university.commands.faculty;

import com.example.university.commands.Command;
import com.example.university.db.GradeDao;
import com.example.university.db.SubjectDao;
import com.example.university.entities.Applicant;
import com.example.university.db.ApplicantDao;
import com.example.university.entities.Grade;
import com.example.university.entities.Subject;
import com.example.university.entities.User;
import com.example.university.db.UserDao;
import com.example.university.utils.Fields;
import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
			return doGet(request);
		}
		return doPost(request);
	}

	/**
	 * Forwards admin to applicant profile.
	 *
	 * @return path to applicant profile page
	 */
	private String doGet(HttpServletRequest request) {
		int userId = Integer.parseInt(request.getParameter("userId"));
		UserDao userDao = new UserDao();
		User user = userDao.find(userId);
		if (user == null) {
			LOG.debug("Can not found user with id = {}", userId);
			return Path.ERROR_PAGE;
		}
		ApplicantDao applicantDao = new ApplicantDao();
		Applicant applicant = applicantDao.find(user);

		List<Grade> grades = new GradeDao().findAllByApplicantId(applicant.getId());
		Map<Grade, Subject> preliminaryGrades = new LinkedHashMap<>();
		Map<Grade, Subject> dimplomaGrades = new LinkedHashMap<>();
		for (Grade g : grades) {
			Subject subject = new SubjectDao().find(g.getSubjectId());
			if (g.getExamType().equals("preliminary")) {
				preliminaryGrades.put(g, subject);
			} else {
				dimplomaGrades.put(g, subject);
			}
		}
		boolean notConfirmed = grades.stream().anyMatch(g -> !g.isConfirmed());
		request.setAttribute("user", user);
		request.setAttribute("applicant", applicant);
		request.setAttribute("preliminaryGrades", preliminaryGrades);
		request.setAttribute("diplomaGrades", dimplomaGrades);
		request.setAttribute("notConfirmed", notConfirmed);
		return Path.FORWARD_APPLICANT_PROFILE;
	}

	/**
	 * Changes blocked status of applicant after submitting button in applicant view
	 *
	 * @return redirects to view applicant page
	 */
	private String doPost(HttpServletRequest request) {
		int applicantId = Integer.parseInt(request.getParameter(Fields.ENTITY_ID));
		ApplicantDao applicantDao = new ApplicantDao();
		Applicant applicant = applicantDao.find(applicantId);
		boolean updatedBlockedStatus = !applicant.getBlockedStatus();
		applicant.setBlockedStatus(updatedBlockedStatus);
		LOG.trace("Applicant with 'id' = {}.Changed 'isBlocked' status = {}"
				+ " record will be updated.", applicantId, updatedBlockedStatus);
		applicantDao.update(applicant);
		return Path.REDIRECT_APPLICANT_PROFILE + applicant.getUserId();
	}

}