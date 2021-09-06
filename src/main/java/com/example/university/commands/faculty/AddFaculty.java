package com.example.university.commands.faculty;

import com.example.university.commands.Command;
import com.example.university.db.FacultyDao;
import com.example.university.db.FacultySubjectsDao;
import com.example.university.db.SubjectDao;
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
import java.util.Collection;


/**
 * Invoked when user wants to add faculty. Command allowed only for admins.
 */
public class AddFaculty extends Command {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(AddFaculty.class);

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
     * Forwards to add page.
     *
     * @return path to the add faculty page.
     */
    private String doGet(HttpServletRequest request) {
        LOG.trace("Request for only showing (not already adding) faculty/add.jsp");
        SubjectDao subjectDao = new SubjectDao();
        Collection<Subject> allSubjects = subjectDao.findAll();
        LOG.trace("All subjects found: {}", allSubjects);
        request.setAttribute("allSubjects", allSubjects);
        LOG.trace("Set request attribute 'allSubjects' = {}", allSubjects);
        return Path.FORWARD_FACULTY_ADD_ADMIN;
    }

    /**
     * Redirects user after submitting add faculty form.
     *
     * @return path to the view of added faculty if fields properly filled,
     * otherwise refreshes add Faculty page.
     */
    private String doPost(HttpServletRequest request) {
        String facultyNameRu = request.getParameter(Fields.FACULTY_NAME_RU);
        String facultyNameEn = request.getParameter(Fields.FACULTY_NAME_EN);
        String facultyTotalPlaces = request
                .getParameter(Fields.FACULTY_TOTAL_PLACES);
        String facultyBudgetPlaces = request
                .getParameter(Fields.FACULTY_BUDGET_PLACES);
        boolean valid = InputValidator.validateFacultyParameters(facultyNameRu,
                facultyNameEn, facultyBudgetPlaces, facultyTotalPlaces);
        if (!valid) {
            request.setAttribute("errorMessage",
                    "Please fill all fields properly!");
            LOG.error("errorMessage: Not all fields are properly filled");
            return Path.REDIRECT_FACULTY_ADD_ADMIN;
        }
        LOG.trace("All fields are properly filled. Start updating database.");
        int totalPlaces = Integer.parseInt(facultyTotalPlaces);
        int budgetPlaces = Integer.parseInt(facultyBudgetPlaces);
        Faculty faculty = new Faculty(facultyNameRu, facultyNameEn,
                budgetPlaces, totalPlaces);
        LOG.trace("Create faculty transfer object: {}", faculty);
        FacultyDao facultyDao = new FacultyDao();
        facultyDao.create(faculty);
        LOG.trace("Create faculty record in database: {}", faculty);
        // only after creating a faculty record we can proceed with
        // adding faculty subjects
        String[] chosenSubjectsIds = request.getParameterValues("subjects");
        if (chosenSubjectsIds != null) {
            FacultySubjectsDao fsd = new FacultySubjectsDao();
            for (String subjectId : chosenSubjectsIds) {
                FacultySubjects facultySubject = new FacultySubjects(
                        Integer.parseInt(subjectId), faculty.getId());
                fsd.create(facultySubject);
                LOG.trace("FacultySubjects record created in database: {}", facultySubject);
            }
        }
        return Path.REDIRECT_TO_FACULTY + facultyNameEn;
    }
}
