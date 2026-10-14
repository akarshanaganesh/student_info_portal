
CREATE DATABASE IF NOT EXISTS student_portal_db;
USE student_portal_db;

-- 1. Users Table (For Secure Session Login)
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'admin',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert Demo Users (Username / Password)
INSERT INTO users (username, password, full_name, role) VALUES
('admin', 'admin123', 'System Administrator', 'admin'),
('faculty', 'faculty123', 'Dr. Rajesh Kumar', 'faculty');

-- 2. Students Table (Student details with image & document paths)
DROP TABLE IF EXISTS students;
CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    roll_no VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    department VARCHAR(50) NOT NULL,
    semester INT NOT NULL,
    cgpa DECIMAL(3, 2),
    photo_path VARCHAR(255),       -- Path or file name of uploaded image
    document_path VARCHAR(255),    -- Path or file name of uploaded marksheet/document
    status VARCHAR(20) DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert Sample Student Records with associated file paths (Total 14 Students)
INSERT INTO students (roll_no, name, email, phone, department, semester, cgpa, photo_path, document_path, status) VALUES
('21CS001', 'Aarav Sharma', 'aarav.sharma@example.com', '9876543210', 'Computer Science', 6, 8.92, 'student1.jpg', 'marksheet1.pdf', 'Active'),
('21CS045', 'Sneha Patel', 'sneha.patel@example.com', '9812345678', 'Information Tech', 6, 9.45, 'student2.jpg', 'resume2.pdf', 'Active'),
('21EC012', 'Rohan Verma', 'rohan.verma@example.com', '9765432109', 'Electronics & Comm', 6, 7.85, 'student3.jpg', NULL, 'Inactive'),
('21ME034', 'Pooja Reddy', 'pooja.reddy@example.com', '9988776655', 'Mechanical Engg', 6, 8.20, NULL, 'certificate4.pdf', 'Active'),
('21CS052', 'Vikram Iyer', 'vikram.iyer@example.com', '9845123456', 'Computer Science', 6, 9.15, 'student5.jpg', 'marksheet5.pdf', 'Active'),
('21IT018', 'Ananya Gupta', 'ananya.gupta@example.com', '9741234567', 'Information Tech', 6, 8.70, 'student6.jpg', 'resume6.pdf', 'Active'),
('21EC025', 'Karthik Nair', 'karthik.nair@example.com', '9632587410', 'Electronics & Comm', 6, 7.90, 'student7.jpg', 'certificate7.pdf', 'Active'),
('21EE008', 'Meera Krishnan', 'meera.k@example.com', '9871236540', 'Electrical Engg', 6, 9.60, 'student8.jpg', 'marksheet8.pdf', 'Active'),
('21ME015', 'Aditya Joshi', 'aditya.joshi@example.com', '9512347890', 'Mechanical Engg', 6, 8.10, 'student9.jpg', 'resume9.pdf', 'Active'),
('21CE003', 'Divya Sundaram', 'divya.s@example.com', '9441237890', 'Civil Engg', 6, 7.65, 'student10.jpg', NULL, 'Inactive'),
('21CS089', 'Siddharth Malhotra', 'sid.m@example.com', '9332145678', 'Computer Science', 6, 9.25, 'student11.jpg', 'certificate11.pdf', 'Active'),
('21IT042', 'Rhea Chawla', 'rhea.chawla@example.com', '9223344556', 'Information Tech', 6, 8.45, 'student12.jpg', 'marksheet12.pdf', 'Active'),
('21AI007', 'Arjun Das', 'arjun.das@example.com', '9112233445', 'Artificial Intelligence', 4, 9.80, NULL, 'resume13.pdf', 'Active'),
('21EC064', 'Neha Kulkarni', 'neha.kulkarni@example.com', '9001122334', 'Electronics & Comm', 6, 8.35, 'student14.jpg', 'marksheet14.pdf', 'Active');
