package com.example.university.commands.profile;

import com.example.university.commands.Command;
import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Invoked when user wants to log out from the system.
 */
public class Logout extends Command {

	private static final long serialVersionUID = -2785976616686657267L;
	private static final Logger LOG = LogManager.getLogger(Logout.class);

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, RequestType requestType)
			throws IOException, ServletException {
		LOG.debug("Start executing Command");
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		LOG.debug("Finished executing Command");
		return Path.WELCOME_PAGE;
	}

}