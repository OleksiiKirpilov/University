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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        FacultyDao facultyDao = new FacultyDao();
        Faculty facultyRecord = facultyDao.find(facultyNameEn);
        List<ApplicantReportSheet> report = new ApplicantReportSheetDao().getFinalizedReport(facultyRecord.getId());
        boolean finalized = !report.isEmpty();
        request.setAttribute(Fields.REPORT_SHEET_FACULTY_FINALIZED, finalized);
        request.setAttribute(Fields.ENTITY_ID, facultyRecord.getId());
        request.setAttribute(Fields.FACULTY_NAME_RU, facultyRecord.getNameRu());
        request.setAttribute(Fields.FACULTY_NAME_EN, facultyRecord.getNameEn());
        request.setAttribute(Fields.FACULTY_TOTAL_PLACES, facultyRecord.getTotalPlaces());
        request.setAttribute(Fields.FACULTY_BUDGET_PLACES, facultyRecord.getBudgetPlaces());

        SubjectDao subjectDao = new SubjectDao();
        List<Subject> facultySubjects = subjectDao.findAllFacultySubjects(facultyRecord);
        request.setAttribute("facultySubjects", facultySubjects);

        HttpSession session = request.getSession(false);
        String role = (String) session.getAttribute("userRole");
        String userEmail = (String) session.getAttribute("user");
        if (Role.isUser(role)) {
            User user = new UserDao().find(userEmail);
            Applicant applicant = new ApplicantDao().find(user);
            boolean applied = hasUserAppliedFacultyByEmail(facultyRecord, applicant);
            request.setAttribute("alreadyApplied", applied);
            boolean enrolled = isApplicantEnrolled(userEmail, report);
            request.setAttribute("enrolled", enrolled);
            return Path.FORWARD_FACULTY_VIEW_USER;
        }
        if (!Role.isAdmin(role)) {
            return Path.FORWARD_FACULTY_VIEW_USER;
        }

        ApplicantDao applicantDao = new ApplicantDao();
        List<Applicant> applicants = applicantDao.findAllFacultyApplicants(facultyRecord);
        Map<Applicant, String> facultyApplicants = new LinkedHashMap<>();
        UserDao userDao = new UserDao();
        for (Applicant applicant : applicants) {
            User user = userDao.find(applicant.getUserId());
            facultyApplicants.put(applicant, user.getFirstName() + " " + user.getLastName());
        }
        request.setAttribute("facultyApplicants", facultyApplicants);
        return Path.FORWARD_FACULTY_VIEW_ADMIN;
    }

    private boolean hasUserAppliedFacultyByEmail(Faculty faculty, Applicant applicant) {
        FacultyApplicants fa = new FacultyApplicants(faculty.getId(), applicant.getId());
        fa = new FacultyApplicantsDao().find(fa);
        return fa != null;
    }

    private boolean isApplicantEnrolled(String email, List<ApplicantReportSheet> report) {
        return report.stream()
                .filter(r -> r.getEmail().equals(email))
                .anyMatch(ApplicantReportSheet::getEntered);
    }

}
