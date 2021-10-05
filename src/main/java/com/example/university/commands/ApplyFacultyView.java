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
        request.setAttribute(Fields.FACULTY_NAME_RU, faculty.getNameRu());
        request.setAttribute(Fields.FACULTY_NAME_EN, faculty.getNameEn());
        request.setAttribute(Fields.FACULTY_TOTAL_PLACES, faculty.getTotalPlaces());
        request.setAttribute(Fields.FACULTY_BUDGET_PLACES, faculty.getBudgetPlaces());
        SubjectDao subjectDao = new SubjectDao();
        List<Subject> facultySubjects = subjectDao.findAllFacultySubjects(faculty);
        request.setAttribute("facultySubjects", facultySubjects);
        Collection<Subject> allSubjects = subjectDao.findAll();
        request.setAttribute("allSubjects", allSubjects);
        return Path.FORWARD_FACULTY_APPLY_USER;
    }

    /**
     * @return redirects user to view of applied faculty if applying is
     * successful, otherwise refreshes this page.
     */
    private String doPost(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String email = String.valueOf(session.getAttribute("user"));
        UserDao userDao = new UserDao();
        User user = userDao.find(email);
        ApplicantDao applicantDao = new ApplicantDao();
        Applicant applicant = applicantDao.find(user);
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
        faDao.create(newFacultyApplicant);
        LOG.trace("FacultyApplicants record was created in database: {}", newFacultyApplicant);
        FacultyDao facultyDao = new FacultyDao();
        Faculty faculty = facultyDao.find(facultyId);
        return Path.REDIRECT_TO_FACULTY + faculty.getNameEn();
    }
}
