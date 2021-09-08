package com.example.university.commands.profile;

import com.example.university.commands.Command;
import com.example.university.db.ApplicantDao;
import com.example.university.db.UserDao;
import com.example.university.entities.Applicant;
import com.example.university.entities.Role;
import com.example.university.entities.User;
import com.example.university.utils.Fields;
import com.example.university.utils.InputValidator;
import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Invoked when user registers in system.
 */
public class UserRegistration extends Command {

    private static final long serialVersionUID = -3071536593627692473L;
    private static final Logger LOG = LogManager.getLogger(UserRegistration.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
        LOG.debug("Executing Command");
        if (RequestType.GET == requestType) {
            return doGet();
        }
        return doPost(request);
    }

    /**
     * Forwards user to user registration page.
     *
     * @return path where page lies
     */
    private String doGet() {
        return Path.FORWARD_USER_REGISTRATION_PAGE;
    }

    /**
     * Registers user in system
     *
     * @return path to welcome page if registration successful, refreshes
     * user registration page otherwise.
     */
    private String doPost(HttpServletRequest request) {
        LOG.debug("Start executing Command");
        String email = request.getParameter(Fields.USER_EMAIL);
        String password = request.getParameter(Fields.USER_PASSWORD);
        String firstName = request.getParameter(Fields.USER_FIRST_NAME);
        String lastName = request.getParameter(Fields.USER_LAST_NAME);
        String lang = request.getParameter(Fields.USER_LANG);
        String city = request.getParameter(Fields.APPLICANT_CITY);
        String district = request.getParameter(Fields.APPLICANT_DISTRICT);
        String school = request.getParameter(Fields.APPLICANT_SCHOOL);
        boolean valid = InputValidator.validateUserParameters(firstName,
                lastName, email, password, lang);
        valid &= InputValidator.validateApplicantParameters(city, district, school);
        if (!valid) {
            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            LOG.error("errorMessage: Not all fields are filled");
            return Path.REDIRECT_USER_REGISTRATION_PAGE;
        }
        User user = new User(email, password, firstName, lastName,
                Role.USER, lang);
        UserDao userDao = new UserDao();
        userDao.create(user);
        LOG.trace("User record created: {}", user);
        Applicant applicant = new Applicant(city, district, school, user);
        ApplicantDao applicantDao = new ApplicantDao();
        applicantDao.create(applicant);
        LOG.trace("Applicant record created: {}", applicant);
        return Path.WELCOME_PAGE;
    }

}
