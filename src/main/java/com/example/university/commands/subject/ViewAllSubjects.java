package com.example.university.commands.subject;

import com.example.university.commands.Command;
import com.example.university.db.SubjectDao;
import com.example.university.entity.Subject;
import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * View all subjects Command.
 */
public class ViewAllSubjects extends Command {

    private static final long serialVersionUID = 19699623476838931L;
    private static final Logger LOG = LogManager.getLogger(ViewAllSubjects.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
        LOG.debug("Executing Command");
        if (requestType == RequestType.GET) {
            return doGet(request);
        }
        return null;
    }

    /**
     * Forwards admin to the view of all subjects.
     *
     * @return path to all subjects view
     */
    private String doGet(HttpServletRequest request) {
        SubjectDao subjectDao = new SubjectDao();
        Collection<Subject> allSubjects = subjectDao.findAll();
        LOG.trace("Subjects records found: {}", allSubjects);
        request.setAttribute("allSubjects", allSubjects);
        LOG.trace("Set the request attribute: 'allSubjects' = {}", allSubjects);
        return Path.FORWARD_SUBJECT_VIEW_ALL_ADMIN;
    }

}
