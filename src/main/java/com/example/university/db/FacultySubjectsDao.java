package com.example.university.db;

import com.example.university.entities.Faculty;
import com.example.university.entities.FacultySubjects;
import com.example.university.utils.Fields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Faculty Subjects DAO. Performs basic read/write operations on Faculty
 * Subjects database table.
 */
public class FacultySubjectsDao extends AbstractDao<FacultySubjects> {

	private static final String FIND_ALL_FACULTY_SUBJECTS =
			"SELECT * FROM faculty_subjects";
	private static final String FIND_FACULTY_SUBJECT =
			"SELECT * FROM faculty_subjects WHERE id = ?";
	private static final String INSERT_FACULTY_SUBJECT =
			"INSERT INTO faculty_subjects (faculty_id, subject_id) VALUES (?,?)";
	private static final String DELETE_FACULTY_SUBJECT =
			"DELETE FROM faculty_subjects WHERE faculty_id = ? AND subject_id = ?";
	private static final String DELETE_ALL_FACULTY_SUBJECTS =
			"DELETE FROM faculty_subjects WHERE faculty_id = ?";

	private static final Logger LOG = LogManager.getLogger(FacultySubjectsDao.class);

	public void create(FacultySubjects entity) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(INSERT_FACULTY_SUBJECT,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, entity.getFacultyId());
			pstmt.setInt(2, entity.getSubjectId());
			pstmt.execute();
			con.commit();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				entity.setId(rs.getInt(Fields.GENERATED_KEY));
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not create a faculty subject", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
	}

	public void delete(FacultySubjects entity) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(DELETE_FACULTY_SUBJECT);
			pstmt.setInt(1, entity.getFacultyId());
			pstmt.setInt(2, entity.getSubjectId());
			pstmt.execute();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not delete a faculty subject", e);
		} finally {
			close(pstmt);
			close(con);
		}
	}

	public void deleteAllSubjects(Faculty entity) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(DELETE_ALL_FACULTY_SUBJECTS);
			pstmt.setInt(1, entity.getId());
			pstmt.execute();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not delete all subjects of a given Faculty", e);
		} finally {
			close(pstmt);
			close(con);
		}
	}

	public FacultySubjects find(int id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FacultySubjects facultySubject = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(FIND_FACULTY_SUBJECT);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			con.commit();
			if (rs.next()) {
				facultySubject = unmarshal(rs);
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not find a faculty subject", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return facultySubject;
	}

	public List<FacultySubjects> findAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<FacultySubjects> facultySubjects = new ArrayList<>();
		try {
			con = getConnection();
			pstmt = con.prepareStatement(FIND_ALL_FACULTY_SUBJECTS);
			rs = pstmt.executeQuery();
			con.commit();
			while (rs.next()) {
				facultySubjects.add(unmarshal(rs));
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not find all faculty subjects", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return facultySubjects;
	}

	/**
	 * Unmarshals specified Faculty Subjects database record to java Faculty
	 * Subjects entity instance.
	 *
	 * @param rs
	 *            - ResultSet record of Faculty Subject
	 * @return entity instance of this record
	 */
	private static FacultySubjects unmarshal(ResultSet rs) {
		FacultySubjects facultySubject = new FacultySubjects();
		try {
			facultySubject.setId(rs.getInt(Fields.ENTITY_ID));
			facultySubject.setFacultyId(rs
					.getInt(Fields.FACULTY_ID));
			facultySubject.setSubjectId(rs
					.getInt(Fields.SUBJECT_ID));
		} catch (SQLException e) {
			LOG.error("Can not unmarshal ResultSet to faculty subject", e);
		}
		return facultySubject;
	}
}
