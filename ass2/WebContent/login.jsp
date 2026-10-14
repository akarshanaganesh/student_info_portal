<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // If user is already logged in, redirect straight to the student list portal
    HttpSession existingSession = request.getSession(false);
    if (existingSession != null && existingSession.getAttribute("currentUser") != null) {
        response.sendRedirect(request.getContextPath() + "/students");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Student Profile Portal</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <header class="navbar">
        <div class="brand">
            🎓 Student Profile Portal
        </div>
        <div>
            <span style="font-size: 0.85rem; color: #64748b;">Secure Authentication Gateway</span>
        </div>
    </header>

    <div class="container login-wrapper">
        <div class="login-card">
            <div class="login-header">
                <h2>Portal Login</h2>
                <p>Sign in with your credentials to view profiles</p>
            </div>

            <!-- Error Notification Alert -->
            <%
                String errorMessage = (String) request.getAttribute("errorMessage");
                String errorParam = request.getParameter("error");
                String msgParam = request.getParameter("msg");

                if (errorMessage != null) {
            %>
                <div class="alert alert-danger">
                    ⚠️ <%= errorMessage %>
                </div>
            <%
                } else if ("unauthorized".equals(errorParam)) {
            %>
                <div class="alert alert-danger">
                    🔒 Session expired or unauthorized! Please log in to access the portal.
                </div>
            <%
                } else if ("logged_out".equals(msgParam)) {
            %>
                <div class="alert alert-success">
                    ✓ You have successfully logged out.
                </div>
            <%
                }
            %>

            <!-- Login Form -->
            <form action="<%= request.getContextPath() %>/LoginServlet" method="POST">
                <div class="form-group">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" id="username" name="username" class="form-input" placeholder="e.g. admin" required autofocus autocomplete="username">
                </div>

                <div class="form-group">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" id="password" name="password" class="form-input" placeholder="Enter your password" required autocomplete="current-password">
                </div>

                <button type="submit" class="btn-primary">Sign In to Portal</button>
            </form>

            <!-- Demo Hint box -->
            <div class="demo-credentials">
                <strong>Default Credentials:</strong><br>
                Username: <code>admin</code> | Password: <code>admin123</code><br>
                Username: <code>faculty</code> | Password: <code>faculty123</code>
            </div>
        </div>
    </div>
</body>
</html>
