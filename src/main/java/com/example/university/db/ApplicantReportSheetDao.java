package com.example.university.db;

import com.example.university.entity.ApplicantReportSheet;
import com.example.university.utils.Fields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Report Sheet DAO. Performs reading the results collected in database view.
 */
public class ApplicantReportSheetDao extends AbstractDao<ApplicantReportSheet> {

    private static final Logger LOG = LogManager.getLogger(ApplicantReportSheetDao.class);

    private static final String GET_REPORT_SHEET =
            "SELECT * FROM faculties_report_sheet WHERE facultyId = ?";


    public List<ApplicantReportSheet> getReport(int facultyId) {
        List<ApplicantReportSheet> applicantsResults = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(GET_REPORT_SHEET);
            pstmt.setInt(1, facultyId);
            rs = pstmt.executeQuery();
            connection.commit();
            while (rs.next()) {
                applicantsResults.add(unmarshal(rs));
            }
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not get report sheet", e);
        } finally {
            close(connection);
            close(pstmt);
            close(rs);
        }
        return applicantsResults;
    }

    private static ApplicantReportSheet unmarshal(ResultSet rs) {
        ApplicantReportSheet report = new ApplicantReportSheet();
        try {
            report.setFacultyId(rs.getInt(Fields.REPORT_SHEET_FACULTY_ID));
            report.setFirstName(rs.getString(Fields.REPORT_SHEET_USER_FIRST_NAME));
            report.setLastName(rs.getString(Fields.REPORT_SHEET_USER_LAST_NAME));
            report.setEmail(rs.getString(Fields.REPORT_SHEET_USER_EMAIL));
            report.setBlockedStatus(rs.getBoolean(Fields.REPORT_SHEET_APPLICANT_IS_BLOCKED));
            report.setPreliminarySum(rs.getInt(Fields.REPORT_SHEET_APPLICANT_PRELIMINARY_SUM));
            report.setDiplomaSum(rs.getInt(Fields.REPORT_SHEET_APPLICANT_DIPLOMA_SUM));
            report.setTotalSum(rs.getInt(Fields.REPORT_SHEET_APPLICANT_TOTAL_SUM));
        } catch (SQLException e) {
            LOG.error("Can not unmarshal ResultSet to report sheet", e);
        }
        return report;
    }
}
