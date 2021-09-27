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


    private static final long serialVersionUID = 4865209265327886367L;
    private static final Logger LOG = LogManager.getLogger(CreateFacultyReport.class);

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

    private String doGet(HttpServletRequest request) {
        return createReport(request, false);
    }

    private String doPost(HttpServletRequest request) {
        return createReport(request, true);
    }

    private String createReport(HttpServletRequest request, boolean saveReport) {
        ApplicantReportSheetDao reportSheetDao = new ApplicantReportSheetDao();
        String id = request.getParameter(Fields.ENTITY_ID);
        int facultyId = Integer.parseInt(id);
        FacultyDao facultyDao = new FacultyDao();
        Faculty faculty = facultyDao.find(facultyId);
        List<ApplicantReportSheet> report = reportSheetDao.getFinalizedReport(facultyId);
        boolean finalized = !report.isEmpty();
        if (!finalized) {
            report = reportSheetDao.getReport(facultyId);
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
            if (saveReport) {
                reportSheetDao.saveAll(report);
                finalized = true;
            }
        } else {
            saveReport = false;
        }
        request.setAttribute(Fields.REPORT_SHEET_FACULTY_FINALIZED, finalized);
        LOG.trace("Set attribute 'finalized': {}", finalized);
        request.setAttribute(Fields.FACULTY_NAME_RU, faculty.getNameRu());
        LOG.trace("Set attribute 'name_ru': {}", faculty.getNameRu());
        request.setAttribute(Fields.FACULTY_NAME_EN, faculty.getNameEn());
        LOG.trace("Set attribute 'name_en': {}", faculty.getNameEn());
        request.setAttribute(Fields.ENTITY_ID, faculty.getId());
        LOG.trace("Set attribute 'id': {}", faculty.getId());
        request.setAttribute("facultyReport", report);
        LOG.trace("Set attribute 'facultyReport': {}", report);
        return saveReport ? Path.REDIRECT_REPORT_SHEET_VIEW + faculty.getId() : Path.FORWARD_REPORT_SHEET_VIEW;
    }

}
