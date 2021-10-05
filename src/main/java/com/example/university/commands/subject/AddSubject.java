package com.example.university.commands.subject;

import com.example.university.commands.Command;
import com.example.university.db.SubjectDao;
import com.example.university.entities.Subject;
import com.example.university.utils.InputValidator;
import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Add subject Command
 */
public class AddSubject extends Command {

    private static final long serialVersionUID = -1505430469675582018L;
    private static final Logger LOG = LogManager.getLogger(AddSubject.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
        LOG.debug("Command execution");
        if (requestType == RequestType.GET) {
            return doGet();
        }
        return doPost(request);
    }

    /**
     * Forwards admin to add subject page.
     *
     * @return path to add subject page
     */
    private String doGet() {
        return Path.FORWARD_SUBJECT_ADD_ADMIN;
    }

    /**
     * Adds subject if fields are properly filled, otherwise redisplays add
     * page.
     *
     * @return view of added subject
     */
    private String doPost(HttpServletRequest request) {
        String nameRu = request.getParameter("name_ru");
        String nameEn = request.getParameter("name_en");
        boolean valid = InputValidator.validateSubjectParameters(nameRu, nameEn);
        if (!valid) {
            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            LOG.debug("errorMessage: Not all fields are properly filled");
            return Path.REDIRECT_SUBJECT_ADD_ADMIN;
        }
        SubjectDao subjectDao = new SubjectDao();
        Subject subject = new Subject();
        subject.setNameRu(nameRu);
        subject.setNameEn(nameEn);
        subjectDao.create(subject);
        LOG.trace("Create subject record in database: {}", subject);
        return Path.REDIRECT_TO_SUBJECT + nameEn;
    }

}
