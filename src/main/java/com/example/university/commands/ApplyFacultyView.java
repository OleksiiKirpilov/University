package com.example.university.commands;

import com.example.university.db.*;
import com.example.university.entities.*;
import com.example.university.utils.Fields;
import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * Invoked when user wants to apply for a faculty.
 */
public class ApplyFacultyView extends Command {

    private static final long serialVersionUID = 8295388021320200832L;
    private static final Logger LOG = LogManager.getLogger(ApplyFacultyView.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
        LOG.debug("Executing Command");
        if (RequestType.GET == requestType) {
            return doGet(request);
        }
        return doPost(request);
    }

    /**
     * Forwards user to apply page of interested faculty.
     *
     * @return path to apply for faculty page
     */
    private String doGet(HttpServletRequest request) {
        String facultyNameEn = request.getParameter(Fields.FACULTY_NAME_EN);
        FacultyDao facultyDao = new FacultyDao();
        Faculty faculty = facultyDao.find(facultyNameEn);
        request.setAttribute(Fields.ENTITY_ID, faculty.getId());
        LOG.trace("Set the request faculty attribute: 'id' = {}", faculty.getId());
        request.setAttribute(Fields.FACULTY_NAME_RU, faculty.getNameRu());
        LOG.trace("Set the request attribute: 'name' = {}", faculty.getNameRu());
        request.setAttribute(Fields.FACULTY_NAME_EN, faculty.getNameEn());
        LOG.trace("Set the request attribute: 'name_en' = {}", faculty.getNameEn());
        request.setAttribute(Fields.FACULTY_TOTAL_PLACES, faculty.getTotalPlaces());
        LOG.trace("Set the request attribute: 'total_places' = {}", faculty.getTotalPlaces());
        request.setAttribute(Fields.FACULTY_BUDGET_PLACES, faculty.getBudgetPlaces());
        LOG.trace("Set the request attribute: 'budget_places' = {}", faculty.getBudgetPlaces());
        SubjectDao subjectDao = new SubjectDao();
        List<Subject> facultySubjects = subjectDao.findAllFacultySubjects(faculty);
        request.setAttribute("facultySubjects", facultySubjects);
        LOG.trace("Set attribute 'facultySubjects': {}", facultySubjects);
        Collection<Subject> allSubjects = subjectDao.findAll();
        request.setAttribute("allSubjects", allSubjects);
        LOG.trace("Set attribute 'allSubjects': {}", allSubjects);
        return Path.FORWARD_FACULTY_APPLY_USER;
    }

    /**
     * @return redirects user to view of applied faculty if applying is
     * successful, otherwise refreshes this page.
     */
    private String doPost(HttpServletRequest request) {
        LOG.trace("Start processing applying for faculty form");
        HttpSession session = request.getSession(false);
        String email = String.valueOf(session.getAttribute("user"));
        UserDao userDao = new UserDao();
        User user = userDao.find(email);
        LOG.trace("Found user in database that wants to apply: {}", user);
        ApplicantDao applicantDao = new ApplicantDao();
        Applicant applicant = applicantDao.find(user);
        LOG.trace("Found applicant record in database for this user: {}", applicant);
        FacultyApplicantsDao faDao = new FacultyApplicantsDao();
        int facultyId = Integer.parseInt(request.getParameter(Fields.ENTITY_ID));
        FacultyApplicants newFacultyApplicant = new FacultyApplicants(facultyId,
                applicant.getId());
        FacultyApplicants existingRecord = faDao.find(newFacultyApplicant);
        if (existingRecord != null) {
            // user is already applied
            LOG.trace("User: {} with Applicant record: {} already applied for faculty with id: {}",
                    user, applicant, facultyId);
            return Path.REDIRECT_TO_VIEW_ALL_FACULTIES;
        }
        LOG.trace("Start extracting data from request");
        Map<String, String[]> parameterMap = request.getParameterMap();
        GradeDao gradeDao = new GradeDao();
        for (Map.Entry<String, String[]> e : parameterMap.entrySet()) {
            String parameterName = e.getKey();
            if (parameterName.endsWith("preliminary") || parameterName.endsWith("diploma")) {
                String[] value = e.getValue();
                int gradeValue = Integer.parseInt(value[0]);
                String[] subjectIdAndExamType = parameterName.split("_");
                int subjectId = Integer.parseInt(subjectIdAndExamType[0]);
                String examType = subjectIdAndExamType[1];
                Grade grade = new Grade(subjectId, applicant.getId(), gradeValue, examType);
                Grade oldGrade = gradeDao.findBySubjectIdAndApplicantIdAndExamType(subjectId, applicant.getId(), examType);
                if (oldGrade == null) {
                    gradeDao.create(grade);
                    LOG.trace("Grade record was created: {}", grade);
                } else if (!oldGrade.isConfirmed()) {
                    gradeDao.update(grade);
                    LOG.trace("Grade record was updated: {}", grade);
                } else {
                    LOG.trace("Grade already exists. {}", oldGrade);
                }
            }
        }
        LOG.trace("Create FacultyApplicants transfer object: {}", newFacultyApplicant);
        faDao.create(newFacultyApplicant);
        LOG.trace("FacultyApplicants record was created in database: {}", newFacultyApplicant);
        LOG.trace("Finished processing applying for faculty form");
        FacultyDao facultyDao = new FacultyDao();
        Faculty faculty = facultyDao.find(facultyId);
        return Path.REDIRECT_TO_FACULTY + faculty.getNameEn();
    }
}
