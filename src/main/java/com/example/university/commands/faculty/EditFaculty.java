package com.example.university.commands.faculty;

import com.example.university.commands.Command;
import com.example.university.db.*;
import com.example.university.entity.Faculty;
import com.example.university.entity.FacultySubjects;
import com.example.university.entity.Subject;
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
        return (RequestType.GET == requestType)
                ? doGet(request, response)
                : doPost(request, response);
    }

    /**
     * Forwards to the edit faculty page
     *
     * @return path to edit page.
     */
    private String doGet(HttpServletRequest request,
                         HttpServletResponse response) {
        String facultyName = request.getParameter(Fields.FACULTY_NAME_EN);
        FacultyDao facultyDao = new FacultyDao();
        Faculty faculty = facultyDao.find(facultyName);
        request.setAttribute(Fields.FACULTY_NAME_RU, faculty.getNameRu());
        LOG.trace("Set attribute 'name_ru': " + faculty.getNameRu());
        request.setAttribute(Fields.FACULTY_NAME_EN, faculty.getNameEn());
        LOG.trace("Set attribute 'name_en': " + faculty.getNameEn());
        request.setAttribute(Fields.FACULTY_TOTAL_PLACES,
                faculty.getTotalPlaces());
        LOG.trace("Set attribute 'total_places': " + faculty.getTotalPlaces());
        request.setAttribute(Fields.FACULTY_BUDGET_PLACES,
                faculty.getBudgetPlaces());
        LOG.trace("Set attribute 'budget_places': " + faculty.getBudgetPlaces());
        SubjectDao subjectDao = new SubjectDao();
        List<Subject> otherSubjects = subjectDao.findAllNotFacultySubjects(faculty);
        request.setAttribute("otherSubjects", otherSubjects);
        LOG.trace("Set attribute 'otherSubjects': " + otherSubjects);
        List<Subject> facultySubjects = subjectDao.findAllFacultySubjects(faculty);
        request.setAttribute("facultySubjects", facultySubjects);
        LOG.trace("Set attribute 'facultySubjects': " + facultySubjects);
        return Path.FORWARD_FACULTY_EDIT_ADMIN;
    }

    /**
     * Edits faculty according to entered data by admin.
     *
     * @return path to the view of edited faculty if succeeded, otherwise
     * redisplays page with <code>doGet</code>
     */
    private String doPost(HttpServletRequest request,
                          HttpServletResponse response) {
        // get parameters from page
        String facultyNameRu = request.getParameter(Fields.FACULTY_NAME_RU);
        LOG.trace("Get parameter 'name_ru' = {}", facultyNameRu);
        String facultyNameEng = request.getParameter(Fields.FACULTY_NAME_EN);
        LOG.trace("Get parameter 'name_en' = {}", facultyNameEng);
        String facultyTotalSeats = request
                .getParameter(Fields.FACULTY_TOTAL_PLACES);
        LOG.trace("Get parameter 'total_places = {}", facultyTotalSeats);
        String facultyBudgetSeats = request
                .getParameter(Fields.FACULTY_BUDGET_PLACES);
        LOG.trace("Get parameter 'budget_places' = {}", facultyBudgetSeats);
        // if user changes faculty name we need to know the old one
        // to update record in db
        String oldFacultyName = request.getParameter("oldName");
        LOG.trace("Get old faculty name from page: {}", oldFacultyName);
        boolean valid = InputValidator.validateFacultyParameters(facultyNameRu,
                facultyNameEng, facultyBudgetSeats, facultyTotalSeats);
        if (!valid) {
            request.setAttribute("errorMessage",
                    "Please fill all fields properly!");
            LOG.error("errorMessage: Not all fields are properly filled");
            return Path.REDIRECT_FACULTY_EDIT_ADMIN + oldFacultyName;
        }
        // if it's true then let's start to update the db
        LOG.trace("All fields are properly filled. Start updating database.");
        int totalSeats = Integer.parseInt(facultyTotalSeats);
        int budgetSeats = Integer.parseInt(facultyBudgetSeats);
        Faculty faculty = new Faculty(facultyNameRu, facultyNameEng,
                budgetSeats, totalSeats);
        FacultyDao facultyDao = new FacultyDao();
        Faculty oldFacultyRecord = facultyDao.find(oldFacultyName);
        faculty.setId(oldFacultyRecord.getId());
        facultyDao.update(faculty);
        LOG.trace("Faculty record updated from: " + oldFacultyRecord
                + ", to: " + faculty);
        String[] oldCheckedSubjectIds = request
                .getParameterValues("oldCheckedSubjects");
        LOG.trace("Get checked subjects before: {}",
                Arrays.toString(oldCheckedSubjectIds));
        String[] newCheckedSubjectsIds = request
                .getParameterValues("subjects");
        LOG.trace("Get checked subjects after: {}",
                Arrays.toString(newCheckedSubjectsIds));
        FacultySubjectsDao facultySubjectsDao = new FacultySubjectsDao();
        if (oldCheckedSubjectIds == null) {
            if (newCheckedSubjectsIds == null) {
                // if before all subjects were unchecked and they are still
                // are
                // then nothing changed - do nothing
                LOG.trace("No faculty subjects records will be changed");
            } else {
                // if user checked something,but before no subjects were
                // checked
                for (String newCheckedSubject : newCheckedSubjectsIds) {
                    int subjectId = Integer.parseInt(newCheckedSubject);
                    FacultySubjects facultySubject = new FacultySubjects(
                            subjectId, faculty.getId());
                    facultySubjectsDao.create(facultySubject);
                    LOG.trace("Faculty subjects record was created: "
                            + facultySubject);
                }
            }
            return Path.REDIRECT_TO_FACULTY + facultyNameEng;
        }
        if (newCheckedSubjectsIds == null) {
            // if user made unchecked all checkbox's and before
            // there
            // were some checked subjects
            LOG.trace("No subjects were checked for this faculty - all records that will be found will be deleted ");
            facultySubjectsDao.deleteAllSubjects(faculty);
            return Path.REDIRECT_TO_FACULTY + facultyNameEng;
        }
        // if there were checked subjects and still are
        // then for INSERT we should check if the record already
        // exists in db
        Set<String> existingRecords = new HashSet<>(
                Arrays.asList(oldCheckedSubjectIds));
        for (String newCheckedSubject : newCheckedSubjectsIds) {
            if (existingRecords.contains(newCheckedSubject)) {
                // if exists - then do nothing
                LOG.trace("This faculty subjects records already exists in db: "
                        + "facultyId = "
                        + faculty.getId()
                        + ", subjectId = " + newCheckedSubject);
            } else {
                // otherwise INSERT
                int subjectId = Integer.parseInt(newCheckedSubject);
                FacultySubjects facultySubject = new FacultySubjects(
                        subjectId, faculty.getId());
                facultySubjectsDao.create(facultySubject);
                LOG.trace("Faculty subjects record was created: "
                        + facultySubject);
            }
        }
        // and check for DELETE records that were previously
        // checked and now are not
        Set<String> newRecords = new HashSet<>(
                Arrays.asList(newCheckedSubjectsIds));
        existingRecords.removeIf(newRecords::contains);
        if (!existingRecords.isEmpty()) {
            for (String subjectToRemove : existingRecords) {
                int subjectId = Integer.parseInt(subjectToRemove);
                FacultySubjects facultySubjectRecordToDelete = new FacultySubjects(
                        subjectId, faculty.getId());
                facultySubjectsDao.delete(facultySubjectRecordToDelete);
                LOG.trace("Faculty subjects record was deleted: "
                        + facultySubjectRecordToDelete);
            }
        }
        return Path.REDIRECT_TO_FACULTY + facultyNameEng;
    }

}
