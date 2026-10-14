<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.portal.model.Student" %>
<%@ page import="com.portal.model.User" %>

<%
    // =========================================================================
    // TASK REQUIREMENT 5: Session Handling - Secure Access Check
    // =========================================================================
    HttpSession activeSession = request.getSession(false);
    if (activeSession == null || activeSession.getAttribute("currentUser") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=unauthorized");
        return;
    }

    User currentUser = (User) activeSession.getAttribute("currentUser");

    @SuppressWarnings("unchecked")
    List<Student> studentList = (List<Student>) request.getAttribute("studentList");

    if (studentList == null) {
        response.sendRedirect(request.getContextPath() + "/students");
        return;
    }

    String currentSort = (String) request.getAttribute("currentSort");
    String currentStatus = (String) request.getAttribute("currentStatus");
    if (currentSort == null) currentSort = "default";
    if (currentStatus == null) currentStatus = "all";

    // Compute stats for sidebar badges
    int activeCount = 0;
    int inactiveCount = 0;
    for (Student s : studentList) {
        if ("Active".equalsIgnoreCase(s.getStatus())) activeCount++;
        else inactiveCount++;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Profiles - Directory</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <!-- Top Secure Header -->
    <header class="navbar">
        <div class="brand">
            🎓 Student Profiles & Records Portal
        </div>
        <div class="user-nav-info">
            <span class="user-badge">
                👤 <%= currentUser.getFullName() %> (<%= currentUser.getRole().toUpperCase() %>)
            </span>
            <a href="<%= request.getContextPath() %>/LogoutServlet" class="btn-logout" title="Sign out of portal">
                🚪 Logout
            </a>
        </div>
    </header>

    <!-- Main Layout Container: Sidebar + Main Content -->
    <div class="app-layout">
        
        <!-- ================================================================= -->
        <!-- SIDE NAVIGATION BAR (Sort & Filter Options)                       -->
        <!-- ================================================================= -->
        <aside class="sidebar">
            
            <!-- Live Search Bar -->
            <div class="sidebar-section">
                <span class="sidebar-title">Quick Search</span>
                <div class="sidebar-search-box">
                    <span class="sidebar-search-icon">🔍</span>
                    <input type="text" id="studentSearch" class="sidebar-search-input" placeholder="Search name or roll..." onkeyup="filterCards()">
                </div>
            </div>

            <!-- Sorting Navigation Section -->
            <div class="sidebar-section">
                <span class="sidebar-title">Sort Profiles</span>
                <ul class="sidebar-menu">
                    <li>
                        <a href="<%= request.getContextPath() %>/students?sort=name" 
                           class="sidebar-link <%= "name".equalsIgnoreCase(currentSort) ? "active" : "" %>">
                            <span class="sidebar-link-content">
                                <span class="sidebar-icon">🔤</span>
                                <span>Sort Alphabetically (A-Z)</span>
                            </span>
                        </a>
                    </li>
                    <li>
                        <a href="<%= request.getContextPath() %>/students?sort=cgpa" 
                           class="sidebar-link <%= "cgpa".equalsIgnoreCase(currentSort) ? "active" : "" %>">
                            <span class="sidebar-link-content">
                                <span class="sidebar-icon">🏆</span>
                                <span>Sort by CGPA (Rank)</span>
                            </span>
                        </a>
                    </li>
                    <li>
                        <a href="<%= request.getContextPath() %>/students" 
                           class="sidebar-link <%= ("default".equalsIgnoreCase(currentSort) && "all".equalsIgnoreCase(currentStatus)) ? "active" : "" %>">
                            <span class="sidebar-link-content">
                                <span class="sidebar-icon">🔢</span>
                                <span>Default (Roll Number)</span>
                            </span>
                        </a>
                    </li>
                </ul>
            </div>

            <!-- Status Filtering Navigation Section -->
            <div class="sidebar-section">
                <span class="sidebar-title">Filter by Status</span>
                <ul class="sidebar-menu">
                    <li>
                        <a href="<%= request.getContextPath() %>/students" 
                           class="sidebar-link <%= ("all".equalsIgnoreCase(currentStatus) && !"name".equalsIgnoreCase(currentSort) && !"cgpa".equalsIgnoreCase(currentSort)) ? "active" : "" %>">
                            <span class="sidebar-link-content">
                                <span class="sidebar-icon">📋</span>
                                <span>All Enrolled Students</span>
                            </span>
                            <span class="sidebar-badge"><%= studentList.size() %></span>
                        </a>
                    </li>
                    <li>
                        <a href="<%= request.getContextPath() %>/students?status=inactive" 
                           class="sidebar-link <%= "inactive".equalsIgnoreCase(currentStatus) ? "active" : "" %>">
                            <span class="sidebar-link-content">
                                <span class="sidebar-icon">⚠️</span>
                                <span>Inactive Students</span>
                            </span>
                            <span class="sidebar-badge" style="color: #991b1b; background-color: #fee2e2;">
                                <%= "inactive".equalsIgnoreCase(currentStatus) ? studentList.size() : "Filter" %>
                            </span>
                        </a>
                    </li>
                    <li>
                        <a href="<%= request.getContextPath() %>/students?status=active" 
                           class="sidebar-link <%= "active".equalsIgnoreCase(currentStatus) ? "active" : "" %>">
                            <span class="sidebar-link-content">
                                <span class="sidebar-icon">✅</span>
                                <span>Active Students</span>
                            </span>
                            <span class="sidebar-badge" style="color: #065f46; background-color: #d1fae5;">
                                <%= "active".equalsIgnoreCase(currentStatus) ? studentList.size() : "Filter" %>
                            </span>
                        </a>
                    </li>
                </ul>
            </div>

            <!-- Sidebar Info Footer -->
            <div class="sidebar-info-card">
                <strong>Portal Directory</strong>
                Use the side menu to sort alphabetically, rank by academic CGPA, or isolate inactive student files.
            </div>

        </aside>

        <!-- ================================================================= -->
        <!-- MAIN CONTENT: DYNAMIC STUDENT PROFILES                            -->
        <!-- ================================================================= -->
        <main class="main-content">
            
            <!-- Page Header with Active Filter Banner -->
            <div class="page-header">
                <div>
                    <h1>Enrolled Student Profiles</h1>
                    <p>Showing student demographic details, verified photos, and academic attachments</p>

                    <!-- Active Filter Pill Indicator -->
                    <% if ("name".equalsIgnoreCase(currentSort)) { %>
                        <div class="active-filter-indicator">
                            <span>🔤 Sorted Alphabetically (A to Z)</span>
                            <a href="<%= request.getContextPath() %>/students" style="color: inherit; text-decoration: none; margin-left: 6px;">✕ Reset</a>
                        </div>
                    <% } else if ("cgpa".equalsIgnoreCase(currentSort)) { %>
                        <div class="active-filter-indicator" style="background-color: #fef3c7; color: #92400e;">
                            <span>🏆 Sorted by Highest CGPA (Academic Rank)</span>
                            <a href="<%= request.getContextPath() %>/students" style="color: inherit; text-decoration: none; margin-left: 6px;">✕ Reset</a>
                        </div>
                    <% } else if ("inactive".equalsIgnoreCase(currentStatus)) { %>
                        <div class="active-filter-indicator" style="background-color: #fee2e2; color: #991b1b;">
                            <span>⚠️ Filtered: Inactive Students Only</span>
                            <a href="<%= request.getContextPath() %>/students" style="color: inherit; text-decoration: none; margin-left: 6px;">✕ Show All</a>
                        </div>
                    <% } else if ("active".equalsIgnoreCase(currentStatus)) { %>
                        <div class="active-filter-indicator" style="background-color: #d1fae5; color: #065f46;">
                            <span>✅ Filtered: Active Students Only</span>
                            <a href="<%= request.getContextPath() %>/students" style="color: inherit; text-decoration: none; margin-left: 6px;">✕ Show All</a>
                        </div>
                    <% } %>
                </div>

                <div class="stats-bar">
                    <div class="stat-pill">
                        Records Displayed: <strong><%= studentList.size() %></strong>
                    </div>
                </div>
            </div>

            <!-- ============================================================= -->
            <!-- Looping and Conditional Logic                                 -->
            <!-- ============================================================= -->
            <% if (studentList.isEmpty()) { %>
                <div class="empty-state">
                    <h3>No Matching Students Found</h3>
                    <p>No student records matched the selected filter or sorting criteria.</p>
                    <a href="<%= request.getContextPath() %>/students" class="btn btn-primary" style="margin-top: 1rem; display: inline-block;">
                        Clear Filters
                    </a>
                </div>
            <% } else { %>
                <div class="student-grid" id="studentGrid">
                    <%
                        for (Student student : studentList) {
                            boolean hasImage = (student.getPhotoPath() != null && !student.getPhotoPath().trim().isEmpty());
                            boolean hasDoc = (student.getDocumentPath() != null && !student.getDocumentPath().trim().isEmpty());
                            boolean isActive = "Active".equalsIgnoreCase(student.getStatus());
                    %>
                        <div class="student-card" data-name="<%= student.getName().toLowerCase() %>" data-roll="<%= student.getRollNo().toLowerCase() %>">
                            <div class="card-top">
                                <!-- <img> tag pointing to ImageStreamServlet -->
                                <div class="profile-img-container">
                                    <img src="<%= request.getContextPath() %>/ImageStreamServlet?id=<%= student.getId() %>" 
                                         alt="Profile photo of <%= student.getName() %>" 
                                         class="profile-img"
                                         loading="lazy">
                                </div>

                                <div class="card-header-info">
                                    <div class="student-name"><%= student.getName() %></div>
                                    <div class="student-roll">ROLL: <%= student.getRollNo() %></div>

                                    <!-- Conditional Status Badge -->
                                    <% if (isActive) { %>
                                        <span class="badge badge-active">● Active</span>
                                    <% } else { %>
                                        <span class="badge badge-inactive">● Inactive</span>
                                    <% } %>

                                    <!-- Conditional Academic Distinction Badge -->
                                    <% if (student.getCgpa() >= 9.0) { %>
                                        <span style="display:block; font-size: 0.725rem; color: #d97706; font-weight: 600; margin-top: 3px;">
                                            ★ High Distinction
                                        </span>
                                    <% } %>
                                </div>
                            </div>

                            <!-- Student Academic and Contact Details -->
                            <div class="card-body">
                                <div class="detail-row">
                                    <span class="detail-label">Department</span>
                                    <span class="detail-value"><%= student.getDepartment() %></span>
                                </div>
                                <div class="detail-row">
                                    <span class="detail-label">Semester</span>
                                    <span class="detail-value">Sem <%= student.getSemester() %></span>
                                </div>
                                <div class="detail-row">
                                    <span class="detail-label">CGPA</span>
                                    <span class="detail-value cgpa-highlight"><%= String.format("%.2f", student.getCgpa()) %></span>
                                </div>
                                <div class="detail-row">
                                    <span class="detail-label">Email</span>
                                    <span class="detail-value"><%= student.getEmail() %></span>
                                </div>
                                <div class="detail-row">
                                    <span class="detail-label">Phone</span>
                                    <span class="detail-value"><%= student.getPhone() != null ? student.getPhone() : "N/A" %></span>
                                </div>
                                <div class="detail-row">
                                    <span class="detail-label">Photo File</span>
                                    <span class="detail-value" style="font-size: 0.8rem; color: #64748b;">
                                        <% if (hasImage) { %>
                                            ✓ <%= student.getPhotoPath() %>
                                        <% } else { %>
                                            <em>Default Avatar</em>
                                        <% } %>
                                    </span>
                                </div>
                            </div>

                            <!-- Document Streaming Links -->
                            <div class="card-footer">
                                <% if (hasDoc) { %>
                                    <a href="<%= request.getContextPath() %>/DocumentStreamServlet?id=<%= student.getId() %>" 
                                       target="_blank" 
                                       class="btn btn-doc"
                                       title="View <%= student.getDocumentPath() %> in new tab">
                                        📄 View Document
                                    </a>

                                    <a href="<%= request.getContextPath() %>/DocumentStreamServlet?id=<%= student.getId() %>&download=true" 
                                       class="btn btn-download"
                                       title="Download <%= student.getDocumentPath() %>">
                                        ⬇ Download
                                    </a>
                                <% } else { %>
                                    <span class="no-doc-badge">
                                        ⚠️ No uploaded document
                                    </span>
                                <% } %>
                            </div>
                        </div>
                    <%
                        }
                    %>
                </div>
            <% } %>

        </main>
    </div>

    <!-- Client-side Real-time Search Script -->
    <script>
        function filterCards() {
            var input = document.getElementById('studentSearch');
            var query = input.value.toLowerCase().trim();
            var cards = document.querySelectorAll('.student-card');

            cards.forEach(function(card) {
                var name = card.getAttribute('data-name') || '';
                var roll = card.getAttribute('data-roll') || '';

                if (name.indexOf(query) !== -1 || roll.indexOf(query) !== -1) {
                    card.style.display = '';
                } else {
                    card.style.display = 'none';
                }
            });
        }
    </script>
</body>
</html>
