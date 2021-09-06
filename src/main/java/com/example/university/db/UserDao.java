package com.example.university.db;

import com.example.university.entity.User;
import com.example.university.utils.Fields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User DAO object. Performs basic read/write operations on User data.
 */
public class UserDao extends AbstractDao<User> {

    private static final String FIND_ALL_USERS = "SELECT * FROM users";
    private static final String FIND_USER = "SELECT * FROM users WHERE users.id = ?";
    private static final String FIND_USER_BY_EMAIL_AND_PASS =
            "SELECT * FROM users WHERE users.email = ? AND users.password = ?";
    private static final String FIND_USER_BY_EMAIL =
            "SELECT * FROM users WHERE users.email = ?";
    private static final String INSERT_USER =
            "INSERT INTO users(users.first_name, users.last_name, users.email," +
                    " users.password, users.role, lang) VALUES (?,?,?,?,?,?);";
    private static final String UPDATE_USER =
            "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ?," +
                    "role = ?, lang = ? WHERE users.id = ?";
    private static final String DELETE_USER =
            "DELETE FROM users WHERE users.id = ?";

    private static final Logger LOG = LogManager
            .getLogger(UserDao.class);


    public void create(User user) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(INSERT_USER,
                    Statement.RETURN_GENERATED_KEYS);
            int counter = 1;
            pstmt.setString(counter++, user.getFirstName());
            pstmt.setString(counter++, user.getLastName());
            pstmt.setString(counter++, user.getEmail());
            pstmt.setString(counter++, user.getPassword());
            pstmt.setString(counter++, user.getRole());
            pstmt.setString(counter, user.getLang());
            pstmt.execute();
            connection.commit();
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(Fields.GENERATED_KEY));
            }
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not create a user", e);
        } finally {
            close(connection);
            close(pstmt);
            close(generatedKeys);
        }
    }

    public void update(User user) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(UPDATE_USER);
            int counter = 1;
            pstmt.setString(counter++, user.getFirstName());
            pstmt.setString(counter++, user.getLastName());
            pstmt.setString(counter++, user.getEmail());
            pstmt.setString(counter++, user.getPassword());
            pstmt.setString(counter++, user.getRole());
            pstmt.setString(counter++, user.getLang());
            pstmt.setBoolean(counter++, user.getActiveStatus());

            pstmt.setInt(counter, user.getId());

            pstmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not update a user", e);
        } finally {
            close(connection);
            close(pstmt);
        }
    }

    public void delete(User user) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(DELETE_USER);
            pstmt.setInt(1, user.getId());

            pstmt.execute();
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not delete a user", e);
        } finally {
            close(connection);
            close(pstmt);
        }
    }

    public User find(int userId) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(FIND_USER);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            connection.commit();
            if (rs.next()) {
                user = unmarshal(rs);
            }
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not find a user by id", e);
        } finally {
            close(connection);
            close(pstmt);
            close(rs);
        }
        return user;
    }

    public User find(String email, String password) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(FIND_USER_BY_EMAIL_AND_PASS);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            connection.commit();
            if (rs.next()) {
                user = unmarshal(rs);
            }
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not find a user by email and password", e);
        } finally {
            close(connection);
            close(pstmt);
            close(rs);
        }
        return user;
    }

    public User find(String email) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(FIND_USER_BY_EMAIL);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            connection.commit();
            if (rs.next()) {
                user = unmarshal(rs);
            }
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not find a user by email", e);
        } finally {
            close(connection);
            close(pstmt);
            close(rs);
        }
        return user;
    }

    public List<User> findAll() {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(FIND_ALL_USERS);
            rs = pstmt.executeQuery();
            connection.commit();
            while (rs.next()) {
                users.add(unmarshal(rs));
            }
        } catch (SQLException e) {
            rollback(connection);
            LOG.error("Can not find all users", e);
        } finally {
            close(connection);
            close(pstmt);
            close(rs);
        }
        return users;
    }

    /**
     * Unmarshals User record in database to Java instance.
     *
     * @param rs - record from result set
     * @return User instance of database record.
     */
    private static User unmarshal(ResultSet rs) {
        User user = new User();
        try {
            user.setId(rs.getInt(Fields.ENTITY_ID));
            user.setFirstName(rs.getString(Fields.USER_FIRST_NAME));
            user.setLastName(rs.getString(Fields.USER_LAST_NAME));
            user.setEmail(rs.getString(Fields.USER_EMAIL));
            user.setPassword(rs.getString(Fields.USER_PASSWORD));
            user.setRole(rs.getString(Fields.USER_ROLE));
            user.setLang(rs.getString(Fields.USER_LANG));
            user.setActiveStatus(rs.getBoolean(Fields.USER_ACTIVE_STATUS));
        } catch (SQLException e) {
            LOG.error("Can not unmarshal result set to user", e);
        }
        return user;
    }
}
