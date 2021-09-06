package com.example.university.db;

import com.example.university.entity.Faculty;
import com.example.university.utils.Fields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Faculty DAO. Performs basic read/write operations on Faculty entity.
 */
public class FacultyDao extends AbstractDao<Faculty> {

    private static final String FIND_ALL_FACULTIES = "SELECT * FROM faculties";
    private static final String FIND_FACULTY_BY_ID =
            "SELECT * FROM faculties WHERE faculties.id = ?";
    private static final String FIND_FACULTY_BY_NAME =
            "SELECT * FROM faculties WHERE faculties.name_ru = ? OR faculties.name_en = ?";
    private static final String INSERT_FACULTY =
            "INSERT INTO faculties(name_ru, name_en, total_places, budget_places) VALUES (?,?,?,?)";
    private static final String UPDATE_FACULTY =
            "UPDATE faculties SET name_ru = ?, name_en= ?, total_places = ?," +
                    "budget_places = ? WHERE id = ?";
    private static final String DELETE_FACULTY =
            "DELETE FROM faculties WHERE id = ?";

    private static final Logger LOG = LogManager.getLogger(FacultyDao.class);


    public void create(Faculty entity) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(INSERT_FACULTY,
                    Statement.RETURN_GENERATED_KEYS);
            int counter = 1;
            pstmt.setString(counter++, entity.getNameRu());
            pstmt.setString(counter++, entity.getNameEn());
            pstmt.setInt(counter++, entity.getTotalPlaces());
            pstmt.setInt(counter, entity.getBudgetPlaces());
            connection.commit();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getInt(Fields.GENERATED_KEY));
            }
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not create a faculty", e);
        } finally {
            close(connection);
            close(pstmt);
            close(rs);
        }
    }

    public void update(Faculty entity) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(UPDATE_FACULTY);
            int counter = 1;
            pstmt.setString(counter++, entity.getNameRu());
            pstmt.setString(counter++, entity.getNameEn());
            pstmt.setInt(counter++, entity.getTotalPlaces());
            pstmt.setInt(counter++, entity.getBudgetPlaces());
            pstmt.setInt(counter, entity.getId());
            pstmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not update a faculty", e);
        } finally {
            close(connection);
            close(pstmt);
        }
    }

    public void delete(Faculty entity) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(DELETE_FACULTY);
            pstmt.setInt(1, entity.getId());
            pstmt.execute();
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not delete a faculty", e);
        } finally {
            close(connection);
            close(pstmt);
        }
    }

    public Faculty find(int id) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Faculty faculty = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(FIND_FACULTY_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            connection.commit();
            if (rs.next()) {
                faculty = unmarshal(rs);
            }
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not find a faculty", e);
        } finally {
            close(connection);
            close(pstmt);
            close(rs);
        }
        return faculty;
    }

    public Faculty find(String facultyName) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Faculty faculty = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(FIND_FACULTY_BY_NAME);
            pstmt.setString(1, facultyName);
            pstmt.setString(2, facultyName);
            rs = pstmt.executeQuery();
            connection.commit();
            if (rs.next()) {
                faculty = unmarshal(rs);
            }
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not find a faculty", e);
        } finally {
            close(connection);
            close(pstmt);
            close(rs);
        }
        return faculty;
    }

    public List<Faculty> findAll() {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Faculty> faculties = new ArrayList<>();
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(FIND_ALL_FACULTIES);
            rs = pstmt.executeQuery();
            connection.commit();
            while (rs.next()) {
                faculties.add(unmarshal(rs));
            }
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not find all faculties", e);
        } finally {
            close(connection);
            close(pstmt);
            close(rs);
        }
        return faculties;
    }

    /**
     * Unmarshals database Faculty record to java Faculty instance.
     *
     * @param rs - ResultSet record
     * @return Faculty instance of this record
     */
    private static Faculty unmarshal(ResultSet rs) {
        Faculty faculty = new Faculty();
        try {
            faculty.setId(rs.getInt(Fields.ENTITY_ID));
            faculty.setNameRu(rs.getString(Fields.FACULTY_NAME_RU));
            faculty.setNameEn(rs.getString(Fields.FACULTY_NAME_EN));
            faculty.setTotalPlaces(rs.getInt(Fields.FACULTY_TOTAL_PLACES));
            faculty.setBudgetPlaces(rs.getInt(Fields.FACULTY_BUDGET_PLACES));
        } catch (SQLException e) {
            LOG.error("Can not unmarshal ResultSet to faculty", e);
        }
        return faculty;
    }
}
