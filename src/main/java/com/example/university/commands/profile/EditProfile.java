package com.example.university.commands.profile;

import com.example.university.commands.Command;
import com.example.university.entities.Applicant;
import com.example.university.db.ApplicantDao;
import com.example.university.entities.Role;
import com.example.university.entities.User;
import com.example.university.db.UserDao;
import com.example.university.utils.Fields;
import com.example.university.utils.InputValidator;
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
 * Edit profile command.
 */
public class EditProfile extends Command {

    private static final long serialVersionUID = -3071536593627692473L;

    private static final Logger LOG = LogManager.getLogger(EditProfile.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
        LOG.debug("Executing Command");
        if (RequestType.GET == requestType) {
            return doGet(request);
        }
        return doPost(request);
    }

    /**
     * Invoked when user wants to edit his page.
     *
     * @return path to the edit profile page.
     */
    private String doGet(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String userEmail = String.valueOf(session.getAttribute("user"));
        String role = String.valueOf(session.getAttribute("userRole"));
        UserDao userDao = new UserDao();
        User user = userDao.find(userEmail);
        request.setAttribute(Fields.USER_FIRST_NAME, user.getFirstName());
        request.setAttribute(Fields.USER_LAST_NAME, user.getLastName());
        request.setAttribute(Fields.USER_EMAIL, user.getEmail());
        request.setAttribute(Fields.USER_PASSWORD, "");
        request.setAttribute(Fields.USER_LANG, user.getLang());
        if (Role.isAdmin(role)) {
            return Path.FORWARD_ADMIN_PROFILE_EDIT;
        }
        if (Role.isUser(role)) {
            ApplicantDao applicantDao = new ApplicantDao();
            Applicant a = applicantDao.find(user);
            request.setAttribute(Fields.APPLICANT_CITY, a.getCity());
            request.setAttribute(Fields.APPLICANT_DISTRICT, a.getDistrict());
            request.setAttribute(Fields.APPLICANT_SCHOOL, a.getSchool());
            request.setAttribute(Fields.APPLICANT_IS_BLOCKED, a.getBlockedStatus());
            return Path.FORWARD_USER_PROFILE_EDIT;
        }
        return null;
    }

    /**
     * Invoked when user already edit his profile and wants to update it.
     *
     * @return path to the user profile if command succeeds, otherwise
     * redisplays editing page.
     */
    private String doPost(HttpServletRequest request) {
        String oldUserEmail = request.getParameter("oldEmail");
        String userFirstName = request.getParameter(Fields.USER_FIRST_NAME);
        String userLastName = request.getParameter(Fields.USER_LAST_NAME);
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String language = request.getParameter("lang");

        boolean valid = InputValidator.validateUserParameters(
                userFirstName, userLastName, email, password, language);
        HttpSession session = request.getSession(false);
        String role = String.valueOf(session.getAttribute("userRole"));

        if (!valid) {
            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            LOG.debug("errorMessage: Not all fields are properly filled");
            return Path.REDIRECT_EDIT_PROFILE;
        }
        UserDao userDao = new UserDao();
        User exisingUser = userDao.find(email);
        User user = userDao.find(oldUserEmail);
        if (exisingUser != null && user.getId() != exisingUser.getId()) {
            setErrorMessage(request, ERROR_EMAIL_USED);
            LOG.debug("This email is already in use.");
            return Path.REDIRECT_EDIT_PROFILE;
        }
        if (!Role.isAdmin(role) && !Role.isUser(role)) {
            return null;
        }

        String school = null;
        String district = null;
        String city = null;
        boolean blockedStatus = false;

        if (Role.isUser(role)) {
            school = request.getParameter(Fields.APPLICANT_SCHOOL);
            district = request.getParameter(Fields.APPLICANT_DISTRICT);
            city = request.getParameter(Fields.APPLICANT_CITY);
            blockedStatus = Boolean.parseBoolean(request.getParameter(Fields.APPLICANT_IS_BLOCKED));
            valid = InputValidator.validateApplicantParameters(city, district, school);
            if (!valid) {
                setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
                LOG.debug("errorMessage: Not all fields are properly filled");
                return Path.REDIRECT_EDIT_PROFILE;
            }
        }
        LOG.trace("User found with such email: {}", user);
        user.setFirstName(userFirstName);
        user.setLastName(userLastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setLang(language);
        LOG.trace("After calling setters with request parameters on user entity: {}", user);
        userDao.update(user);
        LOG.trace("User info updated");
        if (Role.isUser(role)) {
            ApplicantDao applicantDao = new ApplicantDao();
            Applicant a = applicantDao.find(user);
            a.setCity(city);
            a.setDistrict(district);
            a.setSchool(school);
            a.setBlockedStatus(blockedStatus);
            LOG.trace("After calling setters with request parameters on applicant entity: {}", a);
            applicantDao.update(a);
            LOG.trace("Applicant info updated");
        }
        session.setAttribute("user", email);
        session.setAttribute(Fields.USER_LANG, language);
        return Path.REDIRECT_TO_PROFILE;
    }
}