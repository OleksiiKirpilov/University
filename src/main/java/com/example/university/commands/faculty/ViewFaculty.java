package com.example.university.commands.faculty;

import com.example.university.commands.Command;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Invoked when user wants to see some specific faculty.
 */
public class ViewFaculty extends Command {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(ViewFaculty.class);

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
     * Shows page with faculty attributes. Type of action on the page depends on
     * user role.
     *
     * @return path to the view of a faculty.
     */
    private String doGet(HttpServletRequest request) {
        String facultyNameEn = request.getParameter(Fields.FACULTY_NAME_EN);
        LOG.trace("Faculty name to look for is equal to: '{}'", facultyNameEn);
        FacultyDao facultyDao = new FacultyDao();
        Faculty facultyRecord = facultyDao.find(facultyNameEn);
        LOG.trace("Faculty record found: {}", facultyRecord);
        request.setAttribute(Fields.ENTITY_ID, facultyRecord.getId());
        LOG.trace("Set the request attribute: 'id' = {}", facultyRecord.getId());
        request.setAttribute(Fields.FACULTY_NAME_RU, facultyRecord.getNameRu());
        LOG.trace("Set the request attribute: 'name_ru' = {}", facultyRecord.getNameRu());
        request.setAttribute(Fields.FACULTY_NAME_EN, facultyRecord.getNameEn());
        LOG.trace("Set the request attribute: 'name_en' = {}", facultyRecord.getNameEn());
        request.setAttribute(Fields.FACULTY_TOTAL_PLACES, facultyRecord.getTotalPlaces());
        LOG.trace("Set the request attribute: 'total_places' = {}",
                facultyRecord.getTotalPlaces());
        request.setAttribute(Fields.FACULTY_BUDGET_PLACES, facultyRecord.getBudgetPlaces());
        LOG.trace("Set the request attribute: 'budget_places' = {}", facultyRecord.getBudgetPlaces());

        SubjectDao subjectDao = new SubjectDao();
        List<Subject> facultySubjects = subjectDao.findAllFacultySubjects(facultyRecord);

        request.setAttribute("facultySubjects", facultySubjects);
        LOG.trace("Set the request attribute: 'facultySubjects' = {}", facultySubjects);

        HttpSession session = request.getSession(false);
        String role = (String) session.getAttribute("userRole");

        if (role == null || Role.isUser(role)) {
            String userEmail = String.valueOf(session.getAttribute("user"));
            boolean applied = hasUserAppliedFacultyByEmail(facultyRecord, userEmail);
            request.setAttribute("alreadyApplied", applied ? "yes" : "no");
            return Path.FORWARD_FACULTY_VIEW_USER;
        }
        if (!Role.isAdmin(role)) {
            return null;
        }

        ApplicantDao applicantDao = new ApplicantDao();
        List<Applicant> applicants = applicantDao.findAllFacultyApplicants(facultyRecord);
        Map<Applicant, String> facultyApplicants = new TreeMap<>(
                Comparator.comparingInt(Entity::getId));
        UserDao userDao = new UserDao();
        for (Applicant applicant : applicants) {
            User user = userDao.find(applicant.getUserId());
            facultyApplicants.put(applicant,
                    user.getFirstName() + " " + user.getLastName());
        }
        request.setAttribute("facultyApplicants", facultyApplicants);
        LOG.trace("Set the request attribute: 'facultyApplicants' = {}", facultyApplicants);
        return Path.FORWARD_FACULTY_VIEW_ADMIN;
    }

    private boolean hasUserAppliedFacultyByEmail(Faculty faculty, String email) {
        User user = new UserDao().find(email);
        Applicant a = new ApplicantDao().find(user);
        FacultyApplicants fa = new FacultyApplicants(faculty.getId(), a.getId());
        fa = new FacultyApplicantsDao().find(fa);
        return fa != null;
    }

}
