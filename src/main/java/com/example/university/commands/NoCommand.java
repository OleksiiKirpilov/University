package com.example.university.commands;

import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Invoked when no command was found for user request.
 */
public class NoCommand extends Command {

    private static final long serialVersionUID = -2785976616686657267L;

    private static final Logger LOG = LogManager.getLogger(NoCommand.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response,
                          RequestType actionType)
            throws IOException, ServletException {
        LOG.debug("Command execution");
        String errorMessage = "No such command";
        request.setAttribute("errorMessage", errorMessage);
        LOG.error("Set the request attribute: 'errorMessage' = {}", errorMessage);
        return Path.ERROR_PAGE;
    }

}