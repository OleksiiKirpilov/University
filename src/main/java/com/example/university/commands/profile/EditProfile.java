package com.example.university.commands.profile;

import com.example.university.commands.Command;
import com.example.university.entities.Applicant;
import com.example.university.db.ApplicantDao;
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
            return doGet(request, response);
        }
        return doPost(request, response);
    }

    /**
     * Invoked when user wants to edit his page.
     *
     * @return path to the edit profile page.
     */
    private String doGet(HttpServletRequest request,
                         HttpServletResponse response) {
        String result = null;
        HttpSession session = request.getSession(false);
        String userEmail = String.valueOf(session.getAttribute("user"));
        String role = String.valueOf(session.getAttribute("userRole"));
        UserDao userDao = new UserDao();
        User user = userDao.find(userEmail);

        request.setAttribute(Fields.USER_FIRST_NAME, user.getFirstName());
        LOG.trace("Set attribute 'first_name': {}", user.getFirstName());
        request.setAttribute(Fields.USER_LAST_NAME, user.getLastName());
        LOG.trace("Set attribute 'last_name': {}", user.getLastName());
        request.setAttribute(Fields.USER_EMAIL, user.getEmail());
        LOG.trace("Set attribute 'email': {}", user.getEmail());
        request.setAttribute(Fields.USER_PASSWORD, user.getPassword());
        LOG.trace("Set attribute 'password': {}", user.getPassword());
        request.setAttribute(Fields.USER_LANG, user.getLang());
        LOG.trace("Set attribute 'lang': {}", user.getLang());
        if ("admin".equals(role)) {
            return Path.FORWARD_ADMIN_PROFILE_EDIT;
        }
        if ("user".equals(role)) {
            ApplicantDao applicantDao = new ApplicantDao();
            Applicant a = applicantDao.find(user);
            request.setAttribute(Fields.APPLICANT_CITY, a.getCity());
            LOG.trace("Set attribute 'city': {}", a.getCity());
            request.setAttribute(Fields.APPLICANT_DISTRICT, a.getDistrict());
            LOG.trace("Set attribute 'district': {}", a.getDistrict());
            request.setAttribute(Fields.APPLICANT_SCHOOL, a.getSchool());
            LOG.trace("Set attribute 'school': {}", a.getSchool());
            request.setAttribute(Fields.APPLICANT_IS_BLOCKED,
                    a.getBlockedStatus());
            LOG.trace("Set attribute 'isBlocked': {}", a.getBlockedStatus());
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
    private String doPost(HttpServletRequest request,
                          HttpServletResponse response) {
        String oldUserEmail = request.getParameter("oldEmail");
        LOG.trace("Fetch request parameter: 'oldEmail' = {}", oldUserEmail);

        String userFirstName = request.getParameter(Fields.USER_FIRST_NAME);
        LOG.trace("Fetch request parameter: 'first_name' = {}", userFirstName);
        String userLastName = request.getParameter(Fields.USER_LAST_NAME);
        LOG.trace("Fetch request parameter: 'last_name' = {}", userLastName);
        String email = request.getParameter("email");
        LOG.trace("Fetch request parameter: 'email' = {}", email);
        String password = request.getParameter("password");
        LOG.trace("Fetch request parameter: 'password' = {}", password);
        String language = request.getParameter("lang");
        LOG.trace("Fetch request parameter: 'lang' = {}", language);

        boolean valid = InputValidator.validateUserParameters(
                userFirstName, userLastName, email, password, language);

        HttpSession session = request.getSession(false);
        String role = String.valueOf(session.getAttribute("userRole"));

        if (!valid) {
            request.setAttribute("errorMessage",
                    "Please fill all fields properly!");
            LOG.error("errorMessage: Not all fields are properly filled");
            return Path.REDIRECT_EDIT_PROFILE;
        }

        if ("admin".equals(role)) {
            UserDao userDao = new UserDao();
            User user = userDao.find(oldUserEmail);
            LOG.trace("User found with such email: {}", user);
            user.setFirstName(userFirstName);
            user.setLastName(userLastName);
            user.setEmail(email);
            user.setPassword(password);
            user.setLang(language);
            LOG.trace("After calling setters with request parameters on user entity: {}", user);
            userDao.update(user);
            LOG.trace("User info updated");
            // update session attributes if user changed it
            session.setAttribute("user", email);
            session.setAttribute(Fields.USER_LANG, language);
            return Path.REDIRECT_TO_PROFILE;
        }
        if ("user".equals(role)) {
            // if user role is user then we should also update applicant
            // record for them
            String school = request.getParameter(Fields.APPLICANT_SCHOOL);
            LOG.trace("Fetch request parameter: 'school' = {}", school);
            String district = request.getParameter(Fields.APPLICANT_DISTRICT);
            LOG.trace("Fetch request parameter: 'district' = {}", district);
            String city = request.getParameter(Fields.APPLICANT_CITY);
            LOG.trace("Fetch request parameter: 'city' = {}", city);
            boolean blockedStatus = Boolean.parseBoolean(request
                    .getParameter(Fields.APPLICANT_IS_BLOCKED));
            LOG.trace("Fetch request parameter: 'isBlocked' = {}", blockedStatus);
            valid = InputValidator.validateApplicantParameters(city, district, school);
            if (!valid) {
                request.setAttribute("errorMessage",
                        "Please fill all fields properly!");
                LOG.error("errorMessage: Not all fields are properly filled");
                return Path.REDIRECT_EDIT_PROFILE;
            }
            UserDao userDao = new UserDao();
            User user = userDao.find(oldUserEmail);
            LOG.trace("User found with such email: {}", user);
            user.setFirstName(userFirstName);
            user.setLastName(userLastName);
            user.setEmail(email);
            user.setPassword(password);
            user.setLang(language);
            LOG.trace("After calling setters with request parameters on user entity: {}", user);
            userDao.update(user);
            LOG.trace("User info updated");
            ApplicantDao applicantDao = new ApplicantDao();
            Applicant a = applicantDao.find(user);
            a.setCity(city);
            a.setDistrict(district);
            a.setSchool(school);
            a.setBlockedStatus(blockedStatus);
            LOG.trace("After calling setters with request parameters on applicant entity: {}", a);
            applicantDao.update(a);
            LOG.trace("Applicant info updated");
            // update session attributes if user changed it
            session.setAttribute("user", email);
            session.setAttribute(Fields.USER_LANG, language);
            return Path.REDIRECT_TO_PROFILE;
        }
        return null;
    }
}