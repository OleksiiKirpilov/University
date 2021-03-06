package com.example.university.db;

import com.example.university.entities.FacultyApplicants;
import com.example.university.utils.Fields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * Faculty Applicants DAO. Performs basic read/write operations on Faculty
 * Applicants table.
 */
public class FacultyApplicantsDao extends AbstractDao<FacultyApplicants> {

	private static final String FIND_ALL_FACULTY_APPLICANTS =
			"SELECT * FROM faculty_applicants";
	private static final String FIND_FACULTY_APPLICANT_BY_ID =
			"SELECT * FROM faculty_applicants WHERE id = ?";
	private static final String FIND_FACULTY_APPLICANT_BY_FOREIGN_KEYS =
			"SELECT * FROM faculty_applicants WHERE faculty_id = ? AND applicant_id = ?";
	private static final String INSERT_FACULTY_APPLICANT =
			"INSERT INTO faculty_applicants(faculty_id, applicant_id) VALUES (?,?)";
	private static final String DELETE_FACULTY_APPLICANT =
			"DELETE FROM faculty_applicants WHERE id = ?";

	private static final Logger LOG = LogManager.getLogger(FacultyApplicantsDao.class);


	public void create(FacultyApplicants entity) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(INSERT_FACULTY_APPLICANT,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, entity.getFacultyId());
			pstmt.setInt(2, entity.getApplicantId());
			pstmt.execute();
			con.commit();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				entity.setId(rs.getInt(Fields.GENERATED_KEY));
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not create a faculty applicant", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
	}

	public void delete(FacultyApplicants entity) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(DELETE_FACULTY_APPLICANT);
			pstmt.setInt(1, entity.getId());
			pstmt.execute();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not delete a faculty applicant", e);
		} finally {
			close(pstmt);
			close(con);
		}
	}

	public FacultyApplicants find(int id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FacultyApplicants facultyApplicant = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(FIND_FACULTY_APPLICANT_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			con.commit();
			if (rs.next()) {
				facultyApplicant = unmarshal(rs);
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not find a faculty applicant", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return facultyApplicant;
	}

	public FacultyApplicants find(FacultyApplicants fa) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FacultyApplicants facultyApplicant = null;
		try {
			con = getConnection();
			pstmt = con
					.prepareStatement(FIND_FACULTY_APPLICANT_BY_FOREIGN_KEYS);
			pstmt.setInt(1, fa.getFacultyId());
			pstmt.setInt(2, fa.getApplicantId());
			rs = pstmt.executeQuery();
			con.commit();
			if (rs.next()) {
				facultyApplicant = unmarshal(rs);
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not find a faculty applicant", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return facultyApplicant;
	}

	public List<FacultyApplicants> findAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<FacultyApplicants> facultyApplicants = new ArrayList<>();
		try {
			con = getConnection();
			pstmt = con.prepareStatement(FIND_ALL_FACULTY_APPLICANTS);
			rs = pstmt.executeQuery();
			con.commit();
			while (rs.next()) {
				facultyApplicants.add(unmarshal(rs));
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not find all faculty applicants", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return facultyApplicants;
	}

	/**
	 * Unmarshals Faculty Applicant record to java instance.
	 *
	 * @param rs
	 *            - ResultSet record in Faculty Applicants table
	 * @return Faculty Applicant instance of given record
	 */
	private static FacultyApplicants unmarshal(ResultSet rs) {
		FacultyApplicants facultyApplicant = new FacultyApplicants();
		try {
			facultyApplicant.setId(rs.getInt(Fields.ENTITY_ID));
			facultyApplicant.setFacultyId(rs
					.getInt(Fields.FACULTY_ID));
			facultyApplicant.setApplicantId(rs
					.getInt(Fields.APPLICANT_ID));
		} catch (SQLException e) {
			LOG.error("Can not unmarshal ResultSet to faculty applicant", e);
		}
		return facultyApplicant;
	}
}
