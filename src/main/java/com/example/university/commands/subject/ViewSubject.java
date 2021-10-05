package com.example.university.commands.subject;

import com.example.university.commands.Command;
import com.example.university.db.SubjectDao;
import com.example.university.entities.Subject;
import com.example.university.utils.Fields;
import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * View Subject Command
 */
public class ViewSubject extends Command {

    private static final long serialVersionUID = -1129276218825868557L;
    private static final Logger LOG = LogManager.getLogger(ViewSubject.class);

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
     * Forwards admin to the view of some specific subject.
     * @return path to the subject view.
     */
    private String doGet(HttpServletRequest request) {
        String subjectNameEn = request.getParameter(Fields.SUBJECT_NAME_EN);
        SubjectDao subjectDao = new SubjectDao();
        Subject subject = subjectDao.find(subjectNameEn);
        request.setAttribute(Fields.ENTITY_ID, subject.getId());
        request.setAttribute(Fields.SUBJECT_NAME_RU, subject.getNameRu());
        request.setAttribute(Fields.SUBJECT_NAME_EN, subject.getNameEn());
        return Path.FORWARD_SUBJECT_VIEW_ADMIN;
    }

}
