package com.example.university.commands.profile;

import com.example.university.commands.Command;
import com.example.university.db.UserDao;
import com.example.university.entities.User;
import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.example.university.utils.Fields.USER_EMAIL;
import static com.example.university.utils.Fields.USER_PASSWORD;

/**
 * Invoked when user logins in the system.
 */
public class Login extends Command {

    private static final long serialVersionUID = -3071536593627692473L;
    private static final Logger LOG = LogManager.getLogger(Login.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
        LOG.debug("Executing Command");
        if (requestType == RequestType.POST) {
            return doPost(request);
        } else {
            return null;
        }
    }

    /**
     * Logins user in system. As first page displays view of all faculties.
     *
     * @return path to the view of all faculties.
     */
    private String doPost(HttpServletRequest request) {
        String email = request.getParameter(USER_EMAIL);
        String password = request.getParameter(USER_PASSWORD);
        UserDao userDao = new UserDao();
        User user = userDao.find(email, password);
        LOG.trace("User found: {}", user);
        if (user == null) {
            setErrorMessage(request, ERROR_CAN_NOT_FIND_USER);
            LOG.debug("errorMessage: Cannot find user with such login/password");
            return null;
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", user.getEmail());
        session.setAttribute("userRole", user.getRole());
        session.setAttribute("lang", user.getLang());
        return Path.REDIRECT_TO_VIEW_ALL_FACULTIES;
    }

}