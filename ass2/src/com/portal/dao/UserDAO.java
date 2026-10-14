package com.portal.dao;

import com.portal.model.User;
import com.portal.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for User authentication.
 * Fulfills Task Requirement 5: Session Handling and User Authentication.
 */
public class UserDAO {

    /**
     * Validates user credentials against MySQL database.
     * @param username The entered username
     * @param password The entered password
     * @return User object if valid, null if invalid
     */
    public User authenticate(String username, String password) {
        String sql = "SELECT id, username, password, full_name, role FROM users WHERE username = ? AND password = ?";
        User user = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
