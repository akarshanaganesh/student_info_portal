<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Check if session is already active
    HttpSession activeSession = request.getSession(false);
    if (activeSession != null && activeSession.getAttribute("currentUser") != null) {
        response.sendRedirect(request.getContextPath() + "/students");
    } else {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
%>
