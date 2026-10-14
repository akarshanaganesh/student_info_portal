package com.portal.dao;

import com.portal.model.Student;
import com.portal.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Student entities.
 * Executes JDBC queries to fetch student records, sort, and filter from MySQL.
 */
public class StudentDAO {

    /**
     * Retrieves all students from the database.
     * @return List of Student objects
     */
    public List<Student> getAllStudents() {
        return getStudents(null, null);
    }

    /**
     * Retrieves students with optional sorting and status filtering.
     * @param sortBy "name" (A-Z), "cgpa" (Highest first), or "roll" (default)
     * @param statusFilter "Active", "Inactive", or null/"all"
     * @return Filtered and sorted List of Student objects
     */
    public List<Student> getStudents(String sortBy, String statusFilter) {
        List<Student> students = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, roll_no, name, email, phone, department, semester, cgpa, photo_path, document_path, status FROM students ");

        boolean hasWhere = false;
        if (statusFilter != null && !statusFilter.trim().isEmpty() && !statusFilter.equalsIgnoreCase("all")) {
            sql.append("WHERE LOWER(status) = LOWER(?) ");
            hasWhere = true;
        }

        if ("name".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY name ASC");
        } else if ("cgpa".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY cgpa DESC");
        } else {
            sql.append("ORDER BY roll_no ASC");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            if (hasWhere) {
                stmt.setString(1, statusFilter.trim());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student s = new Student();
                    s.setId(rs.getInt("id"));
                    s.setRollNo(rs.getString("roll_no"));
                    s.setName(rs.getString("name"));
                    s.setEmail(rs.getString("email"));
                    s.setPhone(rs.getString("phone"));
                    s.setDepartment(rs.getString("department"));
                    s.setSemester(rs.getInt("semester"));
                    s.setCgpa(rs.getDouble("cgpa"));
                    s.setPhotoPath(rs.getString("photo_path"));
                    s.setDocumentPath(rs.getString("document_path"));
                    s.setStatus(rs.getString("status"));

                    students.add(s);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    /**
     * Retrieves a single student record by their primary key ID.
     * @param id Student primary key
     * @return Student object if found, null otherwise
     */
    public Student getStudentById(int id) {
        String sql = "SELECT id, roll_no, name, email, phone, department, semester, cgpa, photo_path, document_path, status FROM students WHERE id = ?";
        Student s = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    s = new Student();
                    s.setId(rs.getInt("id"));
                    s.setRollNo(rs.getString("roll_no"));
                    s.setName(rs.getString("name"));
                    s.setEmail(rs.getString("email"));
                    s.setPhone(rs.getString("phone"));
                    s.setDepartment(rs.getString("department"));
                    s.setSemester(rs.getInt("semester"));
                    s.setCgpa(rs.getDouble("cgpa"));
                    s.setPhotoPath(rs.getString("photo_path"));
                    s.setDocumentPath(rs.getString("document_path"));
                    s.setStatus(rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return s;
    }
}
