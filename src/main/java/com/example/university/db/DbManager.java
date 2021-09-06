package com.example.university.db;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;


/**
 * Simple database manager.
 * Implements singleton pattern.
 */
public class DbManager {

    private static DbManager instance;
    private final DataSource ds;

    public static synchronized DbManager getInstance() {
        if (instance == null) {
            instance = new DbManager();
        }
        return instance;
    }

    private DbManager() {
        DataSource dataSource = null;
        try {
            dataSource = (DataSource) new InitialContext()
                    .lookup("java:/comp/env/jdbc/university");
        } catch (NamingException e) {
            Logger.getGlobal().severe(e.getMessage());
        }
        ds = dataSource;
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public DataSource getDataSource() {
        return ds;
    }

}
