package com.example.university.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


/**
 * Filter which performs authorization of the user to access resources of the
 * application.
 */
public class AuthFilter implements Filter {

    private static final Logger LOG = LogManager.getLogger(AuthFilter.class);

    // accessible by all users
    private final Set<String> accessibleCommands;
    // accessible only by logged-in users
    private final Set<String> commonCommands;
    // accessible only by user
    private final Set<String> userCommands;
    // accessible only by admin
    private final Set<String> adminCommands;


    public AuthFilter() {
        accessibleCommands = new HashSet<>();
        commonCommands = new HashSet<>();
        userCommands = new HashSet<>();
        adminCommands = new HashSet<>();

        accessibleCommands.add("login");
        accessibleCommands.add("viewFaculty");
        accessibleCommands.add("viewAllFaculties");
        accessibleCommands.add("userRegistration");
        accessibleCommands.add("confirmRegistration");

        // common commands
        commonCommands.add("logout");
        commonCommands.add("viewProfile");
        commonCommands.add("editProfile");
        // user commands
        userCommands.add("applyFaculty");
        // admin commands
        adminCommands.add("adminRegistration");
        adminCommands.add("editFaculty");
        adminCommands.add("addFaculty");
        adminCommands.add("deleteFaculty");
        adminCommands.add("addSubject");
        adminCommands.add("editSubject");
        adminCommands.add("viewAllSubjects");
        adminCommands.add("viewSubject");
        adminCommands.add("viewApplicant");
        adminCommands.add("createReport");
        adminCommands.add("deleteSubject");
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        LOG.debug("Initializing filter: {}", AuthFilter.class.getSimpleName());
    }

    @Override
    public void destroy() {
        LOG.debug("Destroying filter: {}", AuthFilter.class.getSimpleName());
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String command = req.getParameter("command");
        // commands available for all
        if (accessibleCommands.contains(command)) {
            LOG.debug("This command can be accessed by all users: {}", command);
            chain.doFilter(req, res);
            return;
        }
        // commands for authorised users
        LOG.debug("This command can be accessed only by logged in users: {}", command);
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            LOG.debug("Unauthorized access to resource. User is not logged-in.");
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        // command for authorised users and user is logged in
        LOG.debug("User is logged-in. Check common commands to logged in users.");
        if (commonCommands.contains(command)) {
            chain.doFilter(req, res);
            return;
        }
        // command specific to user role
        LOG.debug("Command is specific to user. Check user role.");
        String role = session.getAttribute("userRole").toString();
        // user role allows this command
        if (userCommandByUser(role, command) || adminCommandByAdmin(role, command)) {
            LOG.debug("Command can be executed by this user: {}", command);
            chain.doFilter(req, res);
            return;
        }
        // user role doesn't allow this command
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private boolean userCommandByUser(String role, String command) {
        return "user".equals(role) && userCommands.contains(command);
    }

    private boolean adminCommandByAdmin(String role, String command) {
        return "admin".equals(role) && adminCommands.contains(command);
    }


}
