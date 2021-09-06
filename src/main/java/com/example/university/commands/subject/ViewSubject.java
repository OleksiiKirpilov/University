package com.example.university.commands.subject;

import com.example.university.commands.Command;
import com.example.university.db.SubjectDao;
import com.example.university.entity.Subject;
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
        String subjectNameEng = request.getParameter(Fields.SUBJECT_NAME_EN);
        LOG.trace("Subject name to look for is equal to: '{}'", subjectNameEng);
        SubjectDao subjectDao = new SubjectDao();
        Subject subject = subjectDao.find(subjectNameEng);
        LOG.trace("Subject record found: {}", subject);
        request.setAttribute(Fields.ENTITY_ID, subject.getId());
        LOG.trace("Set the request attribute: 'id' = {}", subject.getId());
        request.setAttribute(Fields.SUBJECT_NAME_RU, subject.getNameRu());
        LOG.trace("Set the request attribute: 'name_ru' = {}", subject.getNameRu());
        request.setAttribute(Fields.SUBJECT_NAME_EN, subject.getNameEn());
        LOG.trace("Set the request attribute: 'name_en' = {}", subject.getNameEn());
        return Path.FORWARD_SUBJECT_VIEW_ADMIN;
    }

}
