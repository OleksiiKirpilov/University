package com.example.university.commands.faculty;

import com.example.university.commands.Command;
import com.example.university.db.GradeDao;
import com.example.university.entities.Grade;
import com.example.university.utils.Fields;
import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Invoked when admin confirms applicants grades.
 */
public class ConfirmGrades extends Command {

    private static final long serialVersionUID = -1706377291835609598L;
    private static final Logger LOG = LogManager.getLogger(AddFaculty.class);


    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response,
                          RequestType requestType) throws IOException, ServletException {
        LOG.debug("Executing Command");
        if (RequestType.POST == requestType) {
            return doPost(request);
        }
        return null;
    }

    private String doPost(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getParameter("userId"));
        int id = Integer.parseInt(request.getParameter("id"));
        new GradeDao().setConfirmedByApplicantId(id, true);
        LOG.trace("Updated confirmed status of grades for applicant_id={}", id);
        return Path.REDIRECT_APPLICANT_PROFILE + userId;
    }
}
