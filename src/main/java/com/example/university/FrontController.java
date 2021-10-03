package com.example.university;

import com.example.university.commands.Command;
import com.example.university.commands.CommandManager;
import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class FrontController extends HttpServlet {

    private static final long serialVersionUID = -6992506231035511735L;

    private static final Logger LOG = LogManager.getLogger(FrontController.class);
    private static final String FINISHED = "Controller processing finished";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response, RequestType.GET);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response, RequestType.POST);
    }

    private void processRequest(HttpServletRequest request,
                                HttpServletResponse response,
                                RequestType requestType) throws IOException, ServletException {
        LOG.debug("Start processing in Controller");
        String commandName = request.getParameter("command");
        Command command = CommandManager.get(commandName);
        LOG.trace("Got 'command' = {}", command);
        String path = command.execute(request, response, requestType);
        if (path == null) {
            LOG.trace("Redirect to address = null");
            LOG.debug(FINISHED);
            response.sendRedirect(Path.WELCOME_PAGE);
            return;
        }
        if (requestType == RequestType.GET) {
            LOG.trace("Forward to address = {}", path);
            LOG.debug(FINISHED);
            request.getRequestDispatcher(path).forward(request, response);
            return;
        }
        LOG.trace("Redirect to address = {}", path);
        LOG.debug(FINISHED);
        response.sendRedirect(path);
    }

}
