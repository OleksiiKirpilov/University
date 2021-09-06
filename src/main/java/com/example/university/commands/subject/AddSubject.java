package com.example.university.commands.subject;

import com.example.university.commands.Command;
import com.example.university.db.SubjectDao;
import com.example.university.entity.Subject;
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
            return doGet(response);
        }
        return doPost(request);
    }

    /**
     * Forwards admin to add page.
     *
     * @return path to add page
     */
    private String doGet(HttpServletResponse response) {
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
        LOG.trace("Fetch request parameter: 'name_ru' = {}", nameRu);
        String nameEn = request.getParameter("name_en");
        LOG.trace("Fetch request parameter: 'name_en' = {}", nameEn);
        boolean valid = InputValidator.validateSubjectParameters(nameRu, nameEn);
        if (!valid) {
            request.setAttribute("errorMessage",
                    "Please fill all fields properly!");
            LOG.error("errorMessage: Not all fields are properly filled");
            return Path.REDIRECT_SUBJECT_ADD_ADMIN;
        }
        SubjectDao subjectDao = new SubjectDao();
        Subject subject = new Subject();
        subject.setNameRu(nameRu);
        subject.setNameEn(nameEn);
        LOG.trace("Create subject transfer object: {}", subject);
        subjectDao.create(subject);
        LOG.trace("Create subject record in database: {}", subject);
        return Path.REDIRECT_TO_SUBJECT + nameEn;
    }

}
