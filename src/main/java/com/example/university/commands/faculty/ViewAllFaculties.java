package com.example.university.commands.faculty;

import com.example.university.commands.Command;
import com.example.university.db.FacultyDao;
import com.example.university.entities.Faculty;
import com.example.university.entities.Role;
import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

/**
 * Invoked when user wants to see all faculties that exist on current time.
 */
public class ViewAllFaculties extends Command {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(ViewAllFaculties.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response,
						  RequestType requestType) throws IOException, ServletException {
        LOG.debug("Executing Command");
        return (requestType == RequestType.GET) ? doGet(request) : null;
    }

    /**
     * Forward user to page of all faculties. View type depends on the user
     * role.
     *
     * @return to view of all faculties
     */
    private String doGet(HttpServletRequest request) {
        String result = null;
        FacultyDao facultyDao = new FacultyDao();
        Collection<Faculty> faculties = facultyDao.findAll();
        LOG.trace("Faculties records found: {}", faculties);
        request.setAttribute("faculties", faculties);
        LOG.trace("Set the request attribute: 'faculties' = {}", faculties);
        HttpSession session = request.getSession(false);
        String role = (String) session.getAttribute("userRole");
        if (role == null || Role.isUser(role)) {
            result = Path.FORWARD_FACULTY_VIEW_ALL_USER;
        } else if (Role.isAdmin(role)) {
            result = Path.FORWARD_FACULTY_VIEW_ALL_ADMIN;
        }
        return result;
    }
}
