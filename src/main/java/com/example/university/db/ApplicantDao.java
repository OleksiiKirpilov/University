package com.example.university.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.university.entities.Applicant;
import com.example.university.entities.Faculty;
import com.example.university.entities.User;
import com.example.university.utils.Fields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Applicant DAO. Performs basic read/write operations on Applicant entity.
 */
public class ApplicantDao extends AbstractDao<Applicant> {

	private static final String FIND_ALL_APPLICANTS = "SELECT * FROM applicants";
	private static final String FIND_APPLICANT =
			"SELECT * FROM applicants WHERE applicants.id = ?";
	private static final String FIND_APPLICANT_BY_USER_ID =
			"SELECT * FROM applicants WHERE applicants.users_id = ?";
	private static final String INSERT_APPLICANT =
			"INSERT INTO applicants(city, district, school, users_id, applicants.isBlocked) VALUES (?,?,?,?,?)";
	private static final String UPDATE_APPLICANT =
			"UPDATE applicants SET city = ?, district = ?, school = ?, users_id = ?," +
					" isBlocked = ? WHERE id = ? ";
	private static final String DELETE_APPLICANT =
			"DELETE FROM applicants WHERE id = ?";
	private static final String FIND_ALL_FACULTY_APPLICANT =
			"SELECT university.applicants.* FROM applicants " +
					"INNER JOIN university.faculty_applicants ON " +
					"university.faculty_applicants.applicant_id = university.applicants.id  " +
					"WHERE faculty_applicants.faculty_id = ?";

	private static final Logger LOG = LogManager.getLogger(ApplicantDao.class);

	public void create(Applicant entity) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(INSERT_APPLICANT,
					Statement.RETURN_GENERATED_KEYS);
			int counter = 1;
			pstmt.setString(counter++, entity.getCity());
			pstmt.setString(counter++, entity.getDistrict());
			pstmt.setString(counter++, entity.getSchool());
			pstmt.setInt(counter++, entity.getUserId());
			pstmt.setBoolean(counter, entity.getBlockedStatus());
			pstmt.execute();
			connection.commit();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				entity.setId(rs.getInt(Fields.GENERATED_KEY));
			}

		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not create an applicant", e);
		} finally {
			close(connection);
			close(pstmt);
			close(rs);
		}
	}

	public void update(Applicant entity) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(UPDATE_APPLICANT);
			int counter = 1;
			pstmt.setString(counter++, entity.getCity());
			pstmt.setString(counter++, entity.getDistrict());
			pstmt.setString(counter++, entity.getSchool());
			pstmt.setInt(counter++, entity.getUserId());
			pstmt.setBoolean(counter++, entity.getBlockedStatus());
			pstmt.setInt(counter, entity.getId());
			pstmt.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not update an applicant", e);
		} finally {
			close(connection);
			close(pstmt);
		}
	}

	public void delete(Applicant entity) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(DELETE_APPLICANT);
			pstmt.setInt(1, entity.getId());
			pstmt.execute();
			connection.commit();
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not delete an applicant", e);
		} finally {
			close(connection);
			close(pstmt);
		}
	}

	public Applicant find(int id) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Applicant applicant = null;
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(FIND_APPLICANT);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			connection.commit();
			if (rs.next()) {
				applicant = unmarshal(rs);
			}
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not find an applicant", e);
		} finally {
			close(connection);
			close(pstmt);
			close(rs);
		}
		return applicant;
	}

	public Applicant find(User user) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Applicant applicant = null;
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(FIND_APPLICANT_BY_USER_ID);
			pstmt.setInt(1, user.getId());
			rs = pstmt.executeQuery();
			connection.commit();
			if (rs.next()) {
				applicant = unmarshal(rs);
			}
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not find an applicant", e);
		} finally {
			close(connection);
			close(pstmt);
			close(rs);
		}
		return applicant;
	}

	public List<Applicant> findAll() {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Applicant> users = new ArrayList<>();
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(FIND_ALL_APPLICANTS);
			rs = pstmt.executeQuery();
			connection.commit();
			while (rs.next()) {
				users.add(unmarshal(rs));
			}
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not find all applicants", e);
		} finally {
			close(connection);
			close(pstmt);
			close(rs);
		}
		return users;
	}

	public List<Applicant> findAllFacultyApplicants(Faculty faculty) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Applicant> facultyApplicants = new ArrayList<>();
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(FIND_ALL_FACULTY_APPLICANT);
			pstmt.setInt(1, faculty.getId());
			rs = pstmt.executeQuery();
			connection.commit();
			while (rs.next()) {
				facultyApplicants.add(unmarshal(rs));
			}
		} catch (SQLException e) {
			rollback(connection);
			LOG.error("Can not find faculty applicants", e);
		} finally {
			close(connection);
			close(pstmt);
			close(rs);
		}
		return facultyApplicants;
	}

	/**
	 * Unmarshals Applicants record in database to Java Applicant instance.
	 *
	 * @param rs
	 *            - ResultSet record
	 * @return Applicant instance of this record
	 */
	private static Applicant unmarshal(ResultSet rs) {
		Applicant a = new Applicant();
		try {
			a.setId(rs.getInt(Fields.ENTITY_ID));
			a.setCity(rs.getString(Fields.APPLICANT_CITY));
			a.setDistrict(rs.getString(Fields.APPLICANT_DISTRICT));
			a.setSchool(rs.getString(Fields.APPLICANT_SCHOOL));
			a.setUserId(rs.getInt(Fields.USER_FOREIGN_KEY_ID));
			a.setBlockedStatus(rs.getBoolean(Fields.APPLICANT_IS_BLOCKED));
		} catch (SQLException e) {
			LOG.error("Can not unmarshal ResultSet to applicant", e);
		}
		return a;
	}
}
