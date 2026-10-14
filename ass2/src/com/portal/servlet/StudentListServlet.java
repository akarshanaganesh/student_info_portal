package com.portal.servlet;

import com.portal.dao.StudentDAO;
import com.portal.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet to fetch student profiles with sorting and filtering options.
 * Supports sorting by Alphabetical name (sort=name), CGPA (sort=cgpa),
 * and filtering by status (status=inactive or status=active).
 */
@WebServlet(name = "StudentListServlet", urlPatterns = {"/StudentListServlet", "/students"})
public class StudentListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StudentDAO studentDAO;

    @Override
    public void init() throws ServletException {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String sortBy = request.getParameter("sort");
            String status = request.getParameter("status");

            List<Student> studentList = studentDAO.getStudents(sortBy, status);

            request.setAttribute("studentList", studentList);
            request.setAttribute("currentSort", sortBy != null ? sortBy : "default");
            request.setAttribute("currentStatus", status != null ? status : "all");

            request.getRequestDispatcher("/students.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error fetching student records from database: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
