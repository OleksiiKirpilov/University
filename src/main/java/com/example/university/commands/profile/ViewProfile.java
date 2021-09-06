package com.example.university.commands.profile;

import com.example.university.commands.Command;
import com.example.university.db.ApplicantDao;
import com.example.university.db.UserDao;
import com.example.university.entity.Applicant;
import com.example.university.entity.User;
import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * View profile command.
 */
public class ViewProfile extends Command {

    private static final long serialVersionUID = -3071536593627692473L;
    private static final Logger LOG = LogManager.getLogger(ViewProfile.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
        LOG.debug("Command execution");
        if (requestType == RequestType.GET) {
            return doGet(request);
        }
        return null;
    }

    /**
     * Forwards user to his profile page, based on his role.
     *
     * @return path to user profile
     */
    private String doGet(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String userEmail = String.valueOf(session.getAttribute("user"));
        UserDao userDao = new UserDao();
        User user = userDao.find(userEmail);
        request.setAttribute("first_name", user.getFirstName());
        LOG.trace("Set the request attribute: 'first_name' = {}", user.getFirstName());
        request.setAttribute("last_name", user.getLastName());
        LOG.trace("Set the request attribute: 'last_name' = {}", user.getLastName());
        request.setAttribute("email", user.getEmail());
        LOG.trace("Set the request attribute: 'email' = {}", user.getEmail());
        request.setAttribute("role", user.getRole());
        LOG.trace("Set the request attribute: 'role' = {}", user.getRole());
        String role = user.getRole();
        if ("admin".equals(role)) {
            return Path.FORWARD_ADMIN_PROFILE;
        }
        if ("user".equals(role)) {
            ApplicantDao applicantDao = new ApplicantDao();
            Applicant applicant = applicantDao.find(user);
            request.setAttribute("city", applicant.getCity());
            LOG.trace("Set the request attribute: 'city' = {}", applicant.getCity());
            request.setAttribute("district", applicant.getDistrict());
            LOG.trace("Set the request attribute: 'district' = {}", applicant.getDistrict());
            request.setAttribute("school", applicant.getSchool());
            LOG.trace("Set the request attribute: 'school' = {}", applicant.getSchool());
            return Path.FORWARD_USER_PROFILE;
        }
        return null;
    }
}