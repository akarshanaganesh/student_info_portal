package com.portal.servlet;

import com.portal.dao.UserDAO;
import com.portal.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet handling user login and session creation.
 * Fulfills Task Requirement 5: Session Handling for secure portal access.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet", "/login"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate input parameters
        if (username != null && !username.trim().isEmpty() &&
            password != null && !password.trim().isEmpty()) {

            User user = userDAO.authenticate(username.trim(), password.trim());

            if (user != null) {
                // Successful authentication: Create HTTP Session
                HttpSession session = request.getSession(true);
                session.setAttribute("currentUser", user);
                session.setAttribute("userRole", user.getRole());
                session.setAttribute("userName", user.getFullName());

                // Set session timeout to 30 minutes (1800 seconds)
                session.setMaxInactiveInterval(30 * 60);

                // Redirect to the student listing servlet
                response.sendRedirect(request.getContextPath() + "/students");
                return;
            }
        }

        // Authentication failed: Return back to login with error message
        request.setAttribute("errorMessage", "Invalid Username or Password! Please try again.");
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
