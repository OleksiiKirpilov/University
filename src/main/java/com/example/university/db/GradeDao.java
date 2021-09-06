package com.example.university.db;

import com.example.university.entity.Grade;
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

    private static final String FIND_ALL_MARKS = "SELECT * FROM grades";
    private static final String FIND_MARK = "SELECT * FROM grades WHERE id = ?";
    private static final String INSERT_MARK =
            "INSERT INTO grades(applicant_id, subject_id, grade, exam_type) VALUES (?,?,?,?)";
    private static final String UPDATE_MARK =
            "UPDATE grades SET applicant_id = ?, subject_id = ?, grade = ?, exam_type = ? " +
                    "WHERE id = ?";
    private static final String DELETE_MARK = "DELETE FROM grades WHERE id = ?";

    private static final Logger LOG = LogManager.getLogger(GradeDao.class);

    public void create(Grade entity) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(INSERT_MARK,
                    Statement.RETURN_GENERATED_KEYS);
            int counter = 1;
            pstmt.setInt(counter++, entity.getApplicantId());
            pstmt.setInt(counter++, entity.getSubjectId());
            pstmt.setInt(counter++, entity.getMark());
            pstmt.setString(counter, entity.getExamType());
            pstmt.execute();
            connection.commit();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getInt(Fields.GENERATED_KEY));
            }
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not create a mark", e);
        } finally {
            close(connection);
            close(pstmt);
            close(rs);
        }
    }

    public void update(Grade entity) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(UPDATE_MARK);
            int counter = 1;
            pstmt.setInt(counter++, entity.getApplicantId());
            pstmt.setInt(counter++, entity.getSubjectId());
            pstmt.setInt(counter++, entity.getMark());
            pstmt.setString(counter++, entity.getExamType());
            pstmt.setInt(counter, entity.getId());
            pstmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not update a mark", e);
        } finally {
            close(connection);
            close(pstmt);
        }
    }

    public void delete(Grade entity) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(DELETE_MARK);
            pstmt.setInt(1, entity.getId());
            pstmt.execute();
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not delete a mark", e);
        } finally {
            close(connection);
            close(pstmt);
        }
    }

    public Grade find(int id) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Grade mark = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(FIND_MARK);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            connection.commit();
            if (rs.next()) {
                mark = unmarshal(rs);
            }
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not find a mark", e);
        } finally {
            close(connection);
            close(pstmt);
            close(rs);
        }
        return mark;
    }

    public List<Grade> findAll() {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Grade> users = new ArrayList<>();
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(FIND_ALL_MARKS);
            rs = pstmt.executeQuery();
            connection.commit();
            while (rs.next()) {
                users.add(unmarshal(rs));
            }
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not find all marks", e);
        } finally {
            close(connection);
            close(pstmt);
            close(rs);
        }
        return users;
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
            g.setMark(rs.getInt(Fields.MARK_VALUE));
            g.setExamType(rs.getString(Fields.MARK_EXAM_TYPE));
        } catch (SQLException e) {
            LOG.error("Can not unmarshal ResultSet to mark", e);
        }
        return g;
    }
}
