package com.example.university.commands.faculty;

import com.example.university.commands.Command;
import com.example.university.entity.Faculty;
import com.example.university.db.FacultyDao;
import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Invoked when user wants to see all faculties that exist on current time.
 */
public class ViewAllFaculties extends Command {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(ViewAllFaculties.class);

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, RequestType requestType)
			throws IOException, ServletException {
		LOG.debug("Executing Command");
		if (requestType == RequestType.GET) {
			return doGet(request, response);
		} else {
			return null;
		}
	}

	/**
	 * Forward user to page of all faculties. View type depends on the user
	 * role.
	 *
	 * @return to view of all facultues
	 */
	private String doGet(HttpServletRequest request,
			HttpServletResponse response) {
		String result = null;
		FacultyDao facultyDao = new FacultyDao();
		Collection<Faculty> faculties = facultyDao.findAll();
		LOG.trace("Faculties records found: {}", faculties);
		request.setAttribute("faculties", faculties);
		LOG.trace("Set the request attribute: 'faculties' = {}", faculties);
		HttpSession session = request.getSession(false);
		String role = (String) session.getAttribute("userRole");
		if (role == null || "user".equals(role)) {
			result = Path.FORWARD_FACULTY_VIEW_ALL_USER;
		} else if ("admin".equals(role)) {
			result = Path.FORWARD_FACULTY_VIEW_ALL_ADMIN;
		}
		return result;
	}
}
