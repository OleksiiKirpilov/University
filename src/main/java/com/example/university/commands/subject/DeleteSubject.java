package com.example.university.commands.subject;

import com.example.university.commands.Command;
import com.example.university.db.FacultySubjectsDao;
import com.example.university.db.GradeDao;
import com.example.university.db.SubjectDao;
import com.example.university.entity.FacultySubjects;
import com.example.university.entity.Grade;
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
import java.util.Collection;


/**
 * Invoked when user wants to delete a subject. Command allowed only for admins.
 */
public class DeleteSubject extends Command {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(DeleteSubject.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
        LOG.debug("Executing Command");
        if (RequestType.POST == requestType) {
            return doPost(request);
        }
        return null;
    }

    /**
     * Redirects user to view of all subjects after submiting a delete button.
     *
     * @return path to view of all subjects if deletion was successful,
     * otherwise to subject view.
     */
    private String doPost(HttpServletRequest request) {
        int subjectId = Integer.parseInt(request.getParameter(Fields.ENTITY_ID));
        SubjectDao subjectDao = new SubjectDao();
        Subject subjectToDelete = subjectDao.find(subjectId);
        LOG.trace("Found subject that should be deleted: {}", subjectToDelete);
        FacultySubjectsDao facultySubjectsDao = new FacultySubjectsDao();
        Collection<FacultySubjects> facultySubjects = facultySubjectsDao.findAll();
        facultySubjects.removeIf(r -> r.getSubjectId() != subjectToDelete.getId());
        String result;
        if (facultySubjects.isEmpty()) {
            LOG.trace("No faculties have this subject as preliminary. Check applicant marks.");
            Collection<Grade> marks = new GradeDao().findAll();
            marks.removeIf(r -> r.getSubjectId() != subjectToDelete.getId());
            if (marks.isEmpty()) {
                LOG.trace("No marks records on this subject. Perform deleting.");
                subjectDao.delete(subjectToDelete);
                result = Path.REDIRECT_TO_VIEW_ALL_SUBJECTS;
            } else {
                LOG.trace("There are marks records that rely on this subject.");
                result = Path.REDIRECT_TO_SUBJECT + subjectToDelete.getNameEn();
            }
            return result;
        }
        LOG.trace("There are faculties that have this subject as preliminary.");
        return Path.REDIRECT_TO_SUBJECT + subjectToDelete.getNameEn();
    }
}
