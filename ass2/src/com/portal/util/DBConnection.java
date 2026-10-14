package com.portal.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to manage MySQL JDBC Database Connection.
 * Fulfills Task Requirement 1: Connecting to MySQL database using JDBC.
 */
public class DBConnection {

    // Database connection credentials
    // NOTE FOR XAMPP USERS:
    // In XAMPP, default username is "root" and password is "" (blank/empty).
    private static final String DB_URL = "jdbc:mysql://localhost:3307/student_portal_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Blank for XAMPP!

    static {
        try {
            // Register MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found in classpath!");
            e.printStackTrace();
        }
    }

    /**
     * Obtains a new connection to the MySQL database.
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Helper method to safely close a connection.
     */
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println(">>> JDBC SUCCESS: Connected to MySQL database student_portal_db!");
        } catch (Exception e) {
            System.err.println(">>> JDBC ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
