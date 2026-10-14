package com.portal.model;

import java.io.Serializable;

/**
 * Model class representing a Student entity.
 * Holds personal details, academic information, and file paths.
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String rollNo;
    private String name;
    private String email;
    private String phone;
    private String department;
    private int semester;
    private double cgpa;
    private String photoPath;     // Image file name/path
    private String documentPath;  // Document/PDF file name/path
    private String status;

    public Student() {
    }

    public Student(int id, String rollNo, String name, String email, String phone,
                   String department, int semester, double cgpa, String photoPath,
                   String documentPath, String status) {
        this.id = id;
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.semester = semester;
        this.cgpa = cgpa;
        this.photoPath = photoPath;
        this.documentPath = documentPath;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", rollNo=" + rollNo + ", name=" + name + ", department=" + department + "]";
    }
}
