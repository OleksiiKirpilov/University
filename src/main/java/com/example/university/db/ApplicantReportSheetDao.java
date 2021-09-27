package com.example.university.db;

import com.example.university.entities.ApplicantReportSheet;
import com.example.university.utils.Fields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Report Sheet DAO. Performs reading the results collected in database view.
 */
public class ApplicantReportSheetDao extends AbstractDao<ApplicantReportSheet> {

    private static final Logger LOG = LogManager.getLogger(ApplicantReportSheetDao.class);

    private static final String GET_REPORT_SHEET =
            "SELECT * FROM faculties_report_sheet WHERE faculty_id = ?";
    private static final String GET_FINALIZED_REPORT_SHEET =
            "SELECT * FROM report_sheet WHERE faculty_id = ?";
    private static final String SAVE_REPORT_SHEET =
            "INSERT INTO report_sheet (" +
                    "faculty_id, first_name, last_name, email, isBlocked, " +
                    "preliminary_sum, diploma_sum, total_sum, " +
                    "entered, entered_on_budget " +
                    " ) VALUES (?,?,?,?,?,?,?,?,?,?)";


    public List<ApplicantReportSheet> getReport(int facultyId) {
        List<ApplicantReportSheet> applicantsResults = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(GET_REPORT_SHEET);
            pstmt.setInt(1, facultyId);
            rs = pstmt.executeQuery();
            con.commit();
            while (rs.next()) {
                applicantsResults.add(unmarshal(rs));
            }
        } catch (SQLException e) {
            rollback(con);
            LOG.error("Can not get report sheet", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return applicantsResults;
    }

    public List<ApplicantReportSheet> getFinalizedReport(int facultyId) {
        List<ApplicantReportSheet> applicantsResults = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(GET_FINALIZED_REPORT_SHEET);
            pstmt.setInt(1, facultyId);
            rs = pstmt.executeQuery();
            con.commit();
            while (rs.next()) {
                ApplicantReportSheet ars = unmarshal(rs);
                applicantsResults.add(unmarshalFinalized(rs, ars));
            }
        } catch (SQLException e) {
            rollback(con);
            LOG.error("Can not get report sheet", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return applicantsResults;
    }

    public void saveAll(List<ApplicantReportSheet> report) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SAVE_REPORT_SHEET,
                    Statement.RETURN_GENERATED_KEYS);
            for (ApplicantReportSheet sheet : report) {
                int counter = 0;
                pstmt.setInt(++counter, sheet.getFacultyId());
                pstmt.setString(++counter, sheet.getFirstName());
                pstmt.setString(++counter, sheet.getLastName());
                pstmt.setString(++counter, sheet.getEmail());
                pstmt.setBoolean(++counter, sheet.getBlockedStatus());
                pstmt.setInt(++counter, sheet.getPreliminarySum());
                pstmt.setInt(++counter, sheet.getDiplomaSum());
                pstmt.setInt(++counter, sheet.getTotalSum());
                pstmt.setBoolean(++counter, sheet.getEntered());
                pstmt.setBoolean(++counter, sheet.getEnteredOnBudget());
                pstmt.execute();
            }
            con.commit();
        } catch (SQLException e) {
            rollback(con);
            LOG.error("Can not save report sheet", e);
        } finally {
            close(pstmt);
            close(con);
        }
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

    private static ApplicantReportSheet unmarshalFinalized(ResultSet rs, ApplicantReportSheet ars) {
        try {
            ars.setEntered(rs.getBoolean(Fields.REPORT_SHEET_APPLICANT_ENTERED));
            ars.setEnteredOnBudget(rs.getBoolean(Fields.REPORT_SHEET_APPLICANT_ENTERED_ON_BUDGET));
        } catch (SQLException e) {
            LOG.error("Can not unmarshal ResultSet to report sheet", e);
        }
        return ars;
    }

}
