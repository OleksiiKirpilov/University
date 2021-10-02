package com.example.university.commands.faculty;

import com.example.university.commands.Command;
import com.example.university.db.ApplicantDao;
import com.example.university.db.FacultyDao;
import com.example.university.db.FacultySubjectsDao;
import com.example.university.db.SubjectDao;
import com.example.university.entities.Applicant;
import com.example.university.entities.Faculty;
import com.example.university.entities.FacultySubjects;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Invoked when admin wants to edit information about some faculty
 */
public class EditFaculty extends Command {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(EditFaculty.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
        LOG.debug("Executing Command");
        return (RequestType.POST == requestType) ? doPost(request) : doGet(request);
    }

    /**
     * Forwards to the edit faculty page
     *
     * @return path to edit page.
     */
    private String doGet(HttpServletRequest request) {
        String facultyName = request.getParameter(Fields.FACULTY_NAME_EN);
        FacultyDao facultyDao = new FacultyDao();
        Faculty faculty = facultyDao.find(facultyName);
        request.setAttribute(Fields.FACULTY_NAME_RU, faculty.getNameRu());
        LOG.trace("Set attribute 'name_ru': {}", faculty.getNameRu());
        request.setAttribute(Fields.FACULTY_NAME_EN, faculty.getNameEn());
        LOG.trace("Set attribute 'name_en': {}", faculty.getNameEn());
        request.setAttribute(Fields.FACULTY_TOTAL_PLACES,
                faculty.getTotalPlaces());
        LOG.trace("Set attribute 'total_places': {}", faculty.getTotalPlaces());
        request.setAttribute(Fields.FACULTY_BUDGET_PLACES,
                faculty.getBudgetPlaces());
        LOG.trace("Set attribute 'budget_places': {}", faculty.getBudgetPlaces());
        SubjectDao subjectDao = new SubjectDao();
        List<Subject> otherSubjects = subjectDao.findAllNotFacultySubjects(faculty);
        request.setAttribute("otherSubjects", otherSubjects);
        LOG.trace("Set attribute 'otherSubjects': {}", otherSubjects);
        List<Subject> facultySubjects = subjectDao.findAllFacultySubjects(faculty);
        request.setAttribute("facultySubjects", facultySubjects);
        LOG.trace("Set attribute 'facultySubjects': {}", facultySubjects);
        return Path.FORWARD_FACULTY_EDIT_ADMIN;
    }

    /**
     * Edits faculty according to data entered by admin.
     *
     * @return path to the view of edited faculty if succeeded, otherwise
     * refreshes page
     */
    private String doPost(HttpServletRequest request) {
        String facultyNameRu = request.getParameter(Fields.FACULTY_NAME_RU);
        String facultyNameEn = request.getParameter(Fields.FACULTY_NAME_EN);
        String facultyTotalPlaces = request.getParameter(Fields.FACULTY_TOTAL_PLACES);
        String facultyBudgetPlaces = request.getParameter(Fields.FACULTY_BUDGET_PLACES);
        // if user changes faculty name we need to know the old one
        // to update record in db
        String oldFacultyName = request.getParameter("oldName");
        boolean valid = InputValidator.validateFacultyParameters(facultyNameRu,
                facultyNameEn, facultyBudgetPlaces, facultyTotalPlaces);
        if (!valid) {
            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            LOG.error("errorMessage: Not all fields are properly filled");
            return Path.REDIRECT_FACULTY_EDIT_ADMIN + oldFacultyName;
        }
        Faculty faculty = createFaculty(facultyNameRu, facultyNameEn, facultyBudgetPlaces, facultyTotalPlaces);
        FacultyDao facultyDao = new FacultyDao();
        Faculty oldFacultyRecord = facultyDao.find(oldFacultyName);
        faculty.setId(oldFacultyRecord.getId());
        List<Applicant> facultyApplicants = new ApplicantDao().findAllFacultyApplicants(faculty);
        if (!facultyApplicants.isEmpty()) {
            setErrorMessage(request, ERROR_FACULTY_DEPENDS);
            return Path.REDIRECT_TO_FACULTY + faculty.getNameEn();
        }
        facultyDao.update(faculty);
        LOG.trace("Faculty record updated from: {}, to: {}",
                oldFacultyRecord, faculty);
        String[] oldCheckedSubjectsIds = request.getParameterValues("oldCheckedSubjects");
        String[] newCheckedSubjectsIds = request.getParameterValues("subjects");
        FacultySubjectsDao facultySubjectsDao = new FacultySubjectsDao();
        if (oldCheckedSubjectsIds == null) {
            if (newCheckedSubjectsIds == null) {
                // if before all subjects were unchecked and they are still
                // are
                // then nothing changed - do nothing
                LOG.trace("No faculty subjects records will be changed");
                return Path.REDIRECT_TO_FACULTY + facultyNameEn;
            }
            // if user checked something,but before no subjects were
            // checked
            for (String newCheckedSubject : newCheckedSubjectsIds) {
                int subjectId = Integer.parseInt(newCheckedSubject);
                FacultySubjects facultySubject = new FacultySubjects(
                        subjectId, faculty.getId());
                facultySubjectsDao.create(facultySubject);
                LOG.trace("Faculty subjects record was created: {}", facultySubject);
            }
            return Path.REDIRECT_TO_FACULTY + facultyNameEn;
        }
        if (newCheckedSubjectsIds == null) {
            // if user unchecked all checkboxes and before
            // there were some checked subjects
            LOG.trace("No subjects were checked for this faculty - all records that will be found will be deleted ");
            facultySubjectsDao.deleteAllSubjects(faculty);
            return Path.REDIRECT_TO_FACULTY + facultyNameEn;
        }
        // if there were checked subjects and still are
        // then for INSERT we should check if the record already
        // exists in db
        Set<String> existingRecords = new HashSet<>(Arrays.asList(oldCheckedSubjectsIds));
        for (String newCheckedSubject : newCheckedSubjectsIds) {
            if (existingRecords.contains(newCheckedSubject)) {
                // if exists - then do nothing
                LOG.trace("This faculty subjects records already exists in db: facultyId = {}, subjectId = {}",
                        faculty.getId(), newCheckedSubject);
            } else {
                // otherwise INSERT
                int subjectId = Integer.parseInt(newCheckedSubject);
                FacultySubjects facultySubject = new FacultySubjects(
                        subjectId, faculty.getId());
                facultySubjectsDao.create(facultySubject);
                LOG.trace("Faculty subjects record was created: {}", facultySubject);
            }
        }
        // and check for DELETE records that were previously
        // checked and now are not
        Set<String> newRecords = new HashSet<>(Arrays.asList(newCheckedSubjectsIds));
        existingRecords.removeIf(newRecords::contains);
        if (!existingRecords.isEmpty()) {
            for (String subjectToRemove : existingRecords) {
                int subjectId = Integer.parseInt(subjectToRemove);
                FacultySubjects facultySubjectRecordToDelete = new FacultySubjects(
                        subjectId, faculty.getId());
                facultySubjectsDao.delete(facultySubjectRecordToDelete);
                LOG.trace("Faculty subjects record was deleted: {}", facultySubjectRecordToDelete);
            }
        }
        return Path.REDIRECT_TO_FACULTY + facultyNameEn;
    }

    private Faculty createFaculty(String facultyNameRu, String facultyNameEn,
                                  String facultyBudgetPlaces, String facultyTotalPlaces) {
        int totalPlaces = Integer.parseInt(facultyTotalPlaces);
        int budgetPlaces = Integer.parseInt(facultyBudgetPlaces);
        return new Faculty(facultyNameRu, facultyNameEn, budgetPlaces, totalPlaces);
    }
}
