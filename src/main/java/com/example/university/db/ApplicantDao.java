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
			"SELECT applicants.* FROM applicants INNER JOIN faculty_applicants" +
					" ON faculty_applicants.applicant_id = applicants.id  " +
					"WHERE faculty_applicants.faculty_id = ?";

	private static final Logger LOG = LogManager.getLogger(ApplicantDao.class);

	public void create(Applicant entity) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(INSERT_APPLICANT,
					Statement.RETURN_GENERATED_KEYS);
			int counter = 0;
			pstmt.setString(++counter, entity.getCity());
			pstmt.setString(++counter, entity.getDistrict());
			pstmt.setString(++counter, entity.getSchool());
			pstmt.setInt(++counter, entity.getUserId());
			pstmt.setBoolean(++counter, entity.getBlockedStatus());
			pstmt.execute();
			con.commit();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				entity.setId(rs.getInt(Fields.GENERATED_KEY));
			}

		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not create an applicant", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
	}

	public void update(Applicant entity) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(UPDATE_APPLICANT);
			int counter = 0;
			pstmt.setString(++counter, entity.getCity());
			pstmt.setString(++counter, entity.getDistrict());
			pstmt.setString(++counter, entity.getSchool());
			pstmt.setInt(++counter, entity.getUserId());
			pstmt.setBoolean(++counter, entity.getBlockedStatus());
			pstmt.setInt(++counter, entity.getId());
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not update an applicant", e);
		} finally {
			close(pstmt);
			close(con);
		}
	}

	public void delete(Applicant entity) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(DELETE_APPLICANT);
			pstmt.setInt(1, entity.getId());
			pstmt.execute();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not delete an applicant", e);
		} finally {
			close(pstmt);
			close(con);
		}
	}

	public Applicant find(int id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Applicant applicant = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(FIND_APPLICANT);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			con.commit();
			if (rs.next()) {
				applicant = unmarshal(rs);
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not find an applicant", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return applicant;
	}

	public Applicant find(User user) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Applicant applicant = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(FIND_APPLICANT_BY_USER_ID);
			pstmt.setInt(1, user.getId());
			rs = pstmt.executeQuery();
			con.commit();
			if (rs.next()) {
				applicant = unmarshal(rs);
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not find an applicant", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return applicant;
	}

	public List<Applicant> findAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Applicant> users = new ArrayList<>();
		try {
			con = getConnection();
			pstmt = con.prepareStatement(FIND_ALL_APPLICANTS);
			rs = pstmt.executeQuery();
			con.commit();
			while (rs.next()) {
				users.add(unmarshal(rs));
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not find all applicants", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return users;
	}

	public List<Applicant> findAllFacultyApplicants(Faculty faculty) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Applicant> facultyApplicants = new ArrayList<>();
		try {
			con = getConnection();
			pstmt = con.prepareStatement(FIND_ALL_FACULTY_APPLICANT);
			pstmt.setInt(1, faculty.getId());
			rs = pstmt.executeQuery();
			con.commit();
			while (rs.next()) {
				facultyApplicants.add(unmarshal(rs));
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not find faculty applicants", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return facultyApplicants;
	}

	/**
	 * Unmarshals Applicants record in database to Applicant instance.
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
