<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error - Student Profile Portal</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <header class="navbar">
        <div class="brand">🎓 Student Profile Portal</div>
    </header>

    <div class="container" style="max-width: 600px; margin-top: 5rem;">
        <div class="login-card">
            <h2 style="color: #ef4444; margin-bottom: 1rem;">⚠️ Application Error</h2>
            <p style="color: #475569; margin-bottom: 1.5rem;">
                <% 
                    String errorMsg = (String) request.getAttribute("errorMessage");
                    if (errorMsg == null && exception != null) {
                        errorMsg = exception.getMessage();
                    }
                    if (errorMsg == null) {
                        errorMsg = "An unexpected error occurred while processing your request.";
                    }
                %>
                <%= errorMsg %>
            </p>
            <a href="<%= request.getContextPath() %>/students" class="btn btn-primary" style="display:inline-block; text-align:center;">
                Return to Dashboard
            </a>
        </div>
    </div>
</body>
</html>
