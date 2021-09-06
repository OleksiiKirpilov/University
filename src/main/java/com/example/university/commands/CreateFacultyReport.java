package com.example.university.commands;

import com.example.university.db.ApplicantReportSheetDao;
import com.example.university.db.FacultyDao;
import com.example.university.entities.ApplicantReportSheet;
import com.example.university.entities.Faculty;
import com.example.university.utils.Fields;
import com.example.university.utils.Path;
import com.example.university.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class CreateFacultyReport extends Command {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(CreateFacultyReport.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
        LOG.debug("Executing Command");
        if (RequestType.GET == requestType) {
            return doGet(request);
        }
        return null;
    }

    private String doGet(HttpServletRequest request) {
        ApplicantReportSheetDao reportSheetDao = new ApplicantReportSheetDao();
        String id = request.getParameter(Fields.ENTITY_ID);
        int facultyId = Integer.parseInt(id);
        List<ApplicantReportSheet> report = reportSheetDao.getReport(facultyId);
        FacultyDao facultyDao = new FacultyDao();
        Faculty faculty = facultyDao.find(facultyId);
        int totalPlaces = faculty.getTotalPlaces();
        int budgetPlaces = faculty.getBudgetPlaces();
        for (int i = 0; i < report.size(); i++) {
            ApplicantReportSheet applicantReport = report.get(i);
            if ((i < totalPlaces) && !applicantReport.getBlockedStatus()) {
                applicantReport.setEntered(true);
                applicantReport.setEnteredOnBudget(i < budgetPlaces);
            } else {
                applicantReport.setEntered(false);
                applicantReport.setEnteredOnBudget(false);
            }
        }
        request.setAttribute(Fields.FACULTY_NAME_RU, faculty.getNameRu());
        LOG.trace("Set attribute 'name_ru': {}", faculty.getNameRu());
        request.setAttribute(Fields.FACULTY_NAME_EN, faculty.getNameEn());
        LOG.trace("Set attribute 'name_en': {}", faculty.getNameEn());
        request.setAttribute("facultyReport", report);
        LOG.trace("Set attribute 'facultyReport': {}", report);
        return Path.FORWARD_REPORT_SHEET_VIEW;
    }

}
