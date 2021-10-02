package com.example.university.db;

import com.example.university.entities.Applicant;
import com.example.university.entities.Grade;
import com.example.university.utils.Fields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Grade DAO. Performs basic read/write operations on Grade database table.
 */
public class GradeDao extends AbstractDao<Grade> {

    private static final String FIND_ALL_GRADES = "SELECT * FROM grades";
    private static final String FIND_ALL_GRADES_BY_APPLICANT_ID =
            "SELECT * FROM grades WHERE applicant_id = ?";
    private static final String FIND_GRADE = "SELECT * FROM grades WHERE id = ?";
    private static final String INSERT_GRADE =
            "INSERT INTO grades(applicant_id, subject_id, grade, exam_type) VALUES (?,?,?,?)";
    private static final String UPDATE_GRADE =
            "UPDATE grades SET applicant_id = ?, subject_id = ?, grade = ?, exam_type = ? " +
                    "WHERE id = ?";
    private static final String DELETE_GRADE = "DELETE FROM grades WHERE id = ?";
    private static final String SET_CONFIRMED = "UPDATE grades SET confirmed = ? WHERE applicant_id = ?";

    private static final Logger LOG = LogManager.getLogger(GradeDao.class);

    public void create(Grade entity) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(INSERT_GRADE,
                    Statement.RETURN_GENERATED_KEYS);
            int counter = 0;
            pstmt.setInt(++counter, entity.getApplicantId());
            pstmt.setInt(++counter, entity.getSubjectId());
            pstmt.setInt(++counter, entity.getGrade());
            pstmt.setString(++counter, entity.getExamType());
            pstmt.execute();
            con.commit();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getInt(Fields.GENERATED_KEY));
            }
        } catch (SQLException e) {
            rollback(con);
            LOG.error("Can not create a grade", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
    }

    public void update(Grade entity) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(UPDATE_GRADE);
            int counter = 0;
            pstmt.setInt(++counter, entity.getApplicantId());
            pstmt.setInt(++counter, entity.getSubjectId());
            pstmt.setInt(++counter, entity.getGrade());
            pstmt.setString(++counter, entity.getExamType());
            pstmt.setInt(++counter, entity.getId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            rollback(con);
            LOG.error("Can not update a grade", e);
        } finally {
            close(pstmt);
            close(con);
        }
    }

    public void delete(Grade entity) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(DELETE_GRADE);
            pstmt.setInt(1, entity.getId());
            pstmt.execute();
            con.commit();
        } catch (SQLException e) {
            rollback(con);
            LOG.error("Can not delete a grade", e);
        } finally {
            close(pstmt);
            close(con);
        }
    }

    public Grade find(int id) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Grade grade = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(FIND_GRADE);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            con.commit();
            if (rs.next()) {
                grade = unmarshal(rs);
            }
        } catch (SQLException e) {
            rollback(con);
            LOG.error("Can not find a grade", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return grade;
    }

    public List<Grade> findAll() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Grade> users = new ArrayList<>();
        try {
            con = getConnection();
            pstmt = con.prepareStatement(FIND_ALL_GRADES);
            rs = pstmt.executeQuery();
            con.commit();
            while (rs.next()) {
                users.add(unmarshal(rs));
            }
        } catch (SQLException e) {
            rollback(con);
            LOG.error("Can not find all grades", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return users;
    }

    public List<Grade> findAllByApplicantId(int id) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Grade> users = new ArrayList<>();
        try {
            con = getConnection();
            pstmt = con.prepareStatement(FIND_ALL_GRADES_BY_APPLICANT_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            con.commit();
            while (rs.next()) {
                users.add(unmarshal(rs));
            }
        } catch (SQLException e) {
            rollback(con);
            LOG.error("Can not find all grades", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return users;
    }

    public void setConfirmedByApplicantId(int applicantId, boolean confirmed) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SET_CONFIRMED);
            pstmt.setBoolean(1, confirmed);
            pstmt.setInt(2, applicantId);
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            rollback(con);
            LOG.error("Can not set confirmed status for grade", e);
        } finally {
            close(pstmt);
            close(con);
        }
    }

    /**
     * Unmarshals database Grade record to Grade java instance.
     *
     * @param rs - ResultSet record in Grades table
     * @return Grade instance of this record
     */
    private static Grade unmarshal(ResultSet rs) {
        Grade g = new Grade();
        try {
            g.setId(rs.getInt(Fields.ENTITY_ID));
            g.setApplicantId(rs.getInt(Fields.APPLICANT_ID));
            g.setSubjectId(rs.getInt(Fields.SUBJECT_ID));
            g.setGrade(rs.getInt(Fields.GRADE_VALUE));
            g.setExamType(rs.getString(Fields.GRADE_EXAM_TYPE));
            g.setConfirmed(rs.getBoolean(Fields.GRADE_CONFIRMED));
        } catch (SQLException e) {
            LOG.error("Can not unmarshal ResultSet to grade", e);
        }
        return g;
    }
}
