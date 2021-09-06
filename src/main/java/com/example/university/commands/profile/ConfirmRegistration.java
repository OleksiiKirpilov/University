package com.example.university.commands.profile;

import com.example.university.commands.Command;
import com.example.university.db.UserDao;
import com.example.university.entity.User;
import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class ConfirmRegistration extends Command {

    private static final long serialVersionUID = -3071536593627692473L;
    private static final Logger LOG = LogManager.getLogger(ConfirmRegistration.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
        LOG.debug("Executing Command");
        if (RequestType.GET == requestType) {
            return doGet(request);
        }
        return null;
    }

    private String doGet(HttpServletRequest request) {

        String encryptedEmail = request.getParameter("ID");
        LOG.trace("Fetch 'ID' parameter from request: {}", encryptedEmail);
        String decodedEmail = new String(Base64.getDecoder().decode(
                encryptedEmail), StandardCharsets.UTF_8);
        LOG.trace("Decode 'ID' to following email: {}", decodedEmail);
        UserDao userDao = new UserDao();
        User user = userDao.find(decodedEmail);
        if (user.getEmail().equals(decodedEmail)) {
            LOG.debug("User with not active status found in database.");
            user.setActiveStatus(true);
            LOG.debug("User active status updated");
            return Path.WELCOME_PAGE;
        } else {
            LOG.error("User not found with such email: {}", decodedEmail);
            return Path.ERROR_PAGE;
        }
    }
}
