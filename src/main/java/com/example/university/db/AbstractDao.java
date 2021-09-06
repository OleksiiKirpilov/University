package com.example.university.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.example.university.entities.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main parent for all DAO objects that working with relational databases.
 * Declares and implements methods for closing Connections, ResultSets and
 * Statements. Also performs roll backing of transaction.
 * @param <T>
 *            type of entity
 */
public abstract class AbstractDao<T extends Entity> {

	private static final Logger LOG = LogManager.getLogger(AbstractDao.class);
	protected final DataSource ds;

	/**
	 * Initializes DataSource object.
	 */
	protected AbstractDao() {
		ds = DbManager.getInstance().getDataSource();
	}

	/**
	 * @return Connection object from the pool.
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	/**
	 * Closes given Connection object.
	 *
	 * @param con
	 *            - connection to be closed
	 */
	protected void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException ex) {
				LOG.error("Cannot commit transaction and close connection", ex);
			}
		}
	}

	/**
	 * Closes given ResultSet object.
	 *
	 * @param rs
	 *            - ResultSet to be closed.
	 */
	protected void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
				LOG.error("Cannot close a result set", ex);
			}
		}
	}

	/**
	 * Closes given Statement object
	 *
	 * @param stmt
	 *            - Statement to be closed
	 */
	protected void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException ex) {
				LOG.error("Cannot close a statement", ex);
			}
		}
	}

	/**
	 * Rollbacks and close the given connection.
	 *
	 * @param con
	 *            Connection to be rolled back and closed.
	 */
	protected void rollback(Connection con) {
		if (con != null) {
			try {
				con.rollback();
			} catch (SQLException ex) {
				LOG.error("Cannot rollback transaction", ex);
			}
		}
	}

}
