package com.example.university.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.university.entities.Faculty;
import com.example.university.entities.Subject;
import com.example.university.utils.Fields;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Subject DAO. Performs basic read/write operations on Subject entity.
 */
public class SubjectDao extends AbstractDao<Subject> {

	private static final String FIND_ALL_SUBJECTS = "SELECT * FROM subjects";
	private static final String FIND_SUBJECT_BY_ID = "SELECT * FROM subjects WHERE id = ?";
	private static final String FIND_SUBJECT_BY_NAME =
			"SELECT * FROM subjects WHERE name_ru = ? OR name_en = ?";
	private static final String INSERT_SUBJECT =
			"INSERT INTO subjects(name_ru, name_en) VALUES(?,?)";
	private static final String UPDATE_SUBJECT =
			"UPDATE subjects SET name_ru = ?, name_en = ? WHERE id = ?";
	private static final String DELETE_SUBJECT =
			"DELETE FROM subjects WHERE id = ?";
	private static final String FIND_ALL_FACULTY_SUBJECTS =
			"SELECT subjects.id, subjects.name_ru, subjects.name_en " +
					"FROM subjects, faculty_subjects " +
					"WHERE faculty_subjects.faculty_id = ?" +
					" AND faculty_subjects.subject_id = subjects.id";
	private static final String FIND_ALL_NOT_FACULTY_SUBJECTS =
			"SELECT subjects.id, subjects.name_ru, subjects.name_en " +
					"FROM subjects " +
					"LEFT JOIN faculty_subjects" +
					" ON faculty_subjects.subject_id = subjects.id" +
					" AND faculty_subjects.faculty_id = ? " +
					"WHERE faculty_subjects.id IS NULL";

	private static final Logger LOG = LogManager.getLogger(SubjectDao.class);

	public void create(Subject entity) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(INSERT_SUBJECT,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, entity.getNameRu());
			pstmt.setString(2, entity.getNameEn());
			pstmt.execute();
			con.commit();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				entity.setId(rs.getInt(Fields.GENERATED_KEY));
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not create a subject", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}

	}

	public void update(Subject entity) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(UPDATE_SUBJECT);
			pstmt.setString(1, entity.getNameRu());
			pstmt.setString(2, entity.getNameEn());
			pstmt.setInt(3, entity.getId());
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not update a subject", e);
		} finally {
			close(pstmt);
			close(con);
		}

	}

	public void delete(Subject entity) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(DELETE_SUBJECT);
			pstmt.setInt(1, entity.getId());
			pstmt.execute();
			con.commit();
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not delete a subject", e);
		} finally {
			close(pstmt);
			close(con);
		}
	}

	public Subject find(int id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Subject subject = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(FIND_SUBJECT_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			con.commit();
			if (rs.next()) {
				subject = unmarshal(rs);
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not find a subject", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return subject;
	}

	public Subject find(String subjectName) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Subject subject = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(FIND_SUBJECT_BY_NAME);
			pstmt.setString(1, subjectName);
			pstmt.setString(2, subjectName);
			rs = pstmt.executeQuery();
			con.commit();
			if (rs.next()) {
				subject = unmarshal(rs);
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not find a subject", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return subject;
	}

	public List<Subject> findAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Subject> subjects = new ArrayList<>();
		try {
			con = getConnection();
			pstmt = con.prepareStatement(FIND_ALL_SUBJECTS);
			rs = pstmt.executeQuery();
			con.commit();
			while (rs.next()) {
				subjects.add(unmarshal(rs));
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not find all subjects", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return subjects;
	}

	public List<Subject> findAllFacultySubjects(Faculty faculty) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Subject> facultySubjects = new ArrayList<>();
		try {
			con = getConnection();
			pstmt = con.prepareStatement(FIND_ALL_FACULTY_SUBJECTS);
			pstmt.setInt(1, faculty.getId());
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

	public List<Subject> findAllNotFacultySubjects(Faculty faculty) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Subject> facultySubjects = new ArrayList<>();
		try {
			con = getConnection();
			pstmt = con.prepareStatement(FIND_ALL_NOT_FACULTY_SUBJECTS);
			pstmt.setInt(1, faculty.getId());
			rs = pstmt.executeQuery();
			con.commit();
			while (rs.next()) {
				facultySubjects.add(unmarshal(rs));
			}
		} catch (SQLException e) {
			rollback(con);
			LOG.error("Can not find all not faculty subjects", e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return facultySubjects;
	}

	/**
	 * Unmarshals database Subject record to Subject instance.
	 *
	 * @param rs
	 *            - ResultSet instance.
	 * @return Subject instance of database record
	 */
	private static Subject unmarshal(ResultSet rs) {
		Subject subject = new Subject();
		try {
			subject.setId(rs.getInt(Fields.ENTITY_ID));
			subject.setNameRu(rs.getString(Fields.SUBJECT_NAME_RU));
			subject.setNameEn(rs.getString(Fields.SUBJECT_NAME_EN));
		} catch (SQLException e) {
			LOG.error("Can not unmarshal ResultSet to subject", e);
		}
		return subject;
	}

}
