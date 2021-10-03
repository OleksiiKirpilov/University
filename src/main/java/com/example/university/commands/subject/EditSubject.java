package com.example.university.commands.subject;

import com.example.university.commands.Command;
import com.example.university.db.SubjectDao;
import com.example.university.entities.Subject;
import com.example.university.utils.Fields;
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
 * Edit subject Command.
 */
public class EditSubject extends Command {

    private static final long serialVersionUID = 2946525838609196070L;
    private static final Logger LOG = LogManager.getLogger(EditSubject.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
        LOG.debug("Command execution");
        if (requestType == RequestType.GET) {
            return doGet(request);
        }
        return doPost(request);
    }

    /**
     * Forwards admin to edit page, so then he can update the subject data.
     *
     * @return path to the edit subject page.
     */
    private String doGet(HttpServletRequest request) {
        String subjectName = request.getParameter(Fields.FACULTY_NAME_EN);
        SubjectDao subjectDao = new SubjectDao();
        Subject subject = subjectDao.find(subjectName);
        request.setAttribute(Fields.SUBJECT_NAME_RU, subject.getNameRu());
        LOG.trace("Set attribute 'name_ru': {}", subject.getNameRu());
        request.setAttribute(Fields.SUBJECT_NAME_EN, subject.getNameEn());
        LOG.trace("Set attribute 'name_en': {}", subject.getNameEn());
        return Path.FORWARD_SUBJECT_EDIT_ADMIN;
    }

    /**
     * Updates subject info.
     *
     * @return path to the view of edited subject if all fields were properly
     * filled, otherwise refreshes edit page.
     */
    private String doPost(HttpServletRequest request) {
        // get parameters from page
        String oldSubjectName = request.getParameter("oldName");
        LOG.trace("Fetch request parameter: 'oldName' = {}", oldSubjectName);
        SubjectDao subjectDao = new SubjectDao();
        Subject subject = subjectDao.find(oldSubjectName);
        LOG.trace("Subject record found with this data: {}", subject);
        String newSubjectNameRu = request.getParameter(Fields.SUBJECT_NAME_RU);
        LOG.trace("Fetch request parameter: 'name_ru' = {}", newSubjectNameRu);
        String newSubjectNameEn = request.getParameter(Fields.SUBJECT_NAME_EN);
        LOG.trace("Fetch request parameter: 'name_en' = {}", newSubjectNameEn);
        boolean valid = InputValidator.validateSubjectParameters(newSubjectNameRu, newSubjectNameEn);
        if (!valid) {
            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            LOG.debug("errorMessage: Not all fields are properly filled");
            return Path.REDIRECT_SUBJECT_EDIT_ADMIN + oldSubjectName;
        }
        subject.setNameRu(newSubjectNameRu);
        subject.setNameEn(newSubjectNameEn);
        LOG.trace("After calling setters with request parameters on subject entity: {}", subject);
        subjectDao.update(subject);
        LOG.trace("Subject record updated");
        return Path.REDIRECT_TO_SUBJECT + newSubjectNameEn;
    }

}
