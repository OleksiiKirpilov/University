package com.example.university.commands.profile;

import com.example.university.commands.Command;
import com.example.university.db.UserDao;
import com.example.university.entities.Role;
import com.example.university.entities.User;
import com.example.university.utils.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Invoked when administrator wants to add another admin user.
 */
public class AdminRegistration extends Command {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(AdminRegistration.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
        LOG.debug("Executing Command");
        if (requestType == RequestType.GET) {
            return doGet();
        }
        return doPost(request);
    }

    /**
     * Forwards user to registration admin page.
     *
     * @return path where lie this page
     */
    private String doGet() {
        return Path.FORWARD_ADMIN_REGISTRATION_PAGE;
    }

    /**
     * If validation is successful then admin record will be added in database.
     *
     * @return after registration will be completed returns path to welcome
     * page, if not then doGet method will be called.
     */
    private String doPost(HttpServletRequest request) {
        String email = request.getParameter(Fields.USER_EMAIL);
        String password = request.getParameter(Fields.USER_PASSWORD);
        String firstName = request.getParameter(Fields.USER_FIRST_NAME);
        String lastName = request.getParameter(Fields.USER_LAST_NAME);
        String lang = request.getParameter(Fields.USER_LANG);
        boolean valid = InputValidator.validateUserParameters(firstName,
                lastName, email, password, lang);
        if (!valid) {
            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            LOG.error("errorMessage: Not all fields are filled");
            return Path.REDIRECT_ADMIN_REGISTRATION_PAGE;
        }
        User user = new User(email, password, null, firstName, lastName, Role.ADMIN, lang);
        UserDao userDao = new UserDao();
        userDao.create(user);
        LOG.trace("User record created: {}", user);
        setOkMessage(request, MESSAGE_ACCOUNT_CREATED);
        return Path.REDIRECT_TO_PROFILE;

    }
}
