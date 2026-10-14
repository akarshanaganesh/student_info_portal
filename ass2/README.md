# Student Profile Portal (Java Servlets & JSP with MySQL JDBC)

A secure J2EE / Jakarta EE web application allowing users to view dynamic student profiles along with their uploaded images and documents.

---

## 📌 Task Checklist & Implementation Details

| Req # | Task Description | Implementation File(s) | Description |
|---|---|---|---|
| **1** | **Connect to MySQL using JDBC** to fetch student details and associated file paths | [`DBConnection.java`](file:///c:/Users/HP/OneDrive/Desktop/ass2/src/com/portal/util/DBConnection.java), [`StudentDAO.java`](file:///c:/Users/HP/OneDrive/Desktop/ass2/src/com/portal/dao/StudentDAO.java), [`database.sql`](file:///c:/Users/HP/OneDrive/Desktop/ass2/database.sql) | Connects via `com.mysql.cj.jdbc.Driver` and queries student data including `photo_path` and `document_path`. |
| **2** | **Retrieve and stream images/documents** from the folder where they are stored | [`ImageStreamServlet.java`](file:///c:/Users/HP/OneDrive/Desktop/ass2/src/com/portal/servlet/ImageStreamServlet.java), [`DocumentStreamServlet.java`](file:///c:/Users/HP/OneDrive/Desktop/ass2/src/com/portal/servlet/DocumentStreamServlet.java) | Reads binary files from `uploads/images` and `uploads/docs`, sets MIME types, and streams bytes to `ServletOutputStream`. |
| **3** | **Display student profiles dynamically** in an HTML/JSP page with `<img>` tags pointing to the Servlet | [`students.jsp`](file:///c:/Users/HP/OneDrive/Desktop/ass2/WebContent/students.jsp) | Profile cards render photos via `<img src="ImageStreamServlet?id=<%= student.getId() %>" />`. |
| **4** | **Looping and conditional logic** to list multiple students and their files | [`students.jsp`](file:///c:/Users/HP/OneDrive/Desktop/ass2/WebContent/students.jsp) | Employs `for` loop over `studentList`, conditional checks for active status, missing images (fallback SVG avatar), and document availability. |
| **5** | **Session handling** so only logged-in users access the portal securely | [`LoginServlet.java`](file:///c:/Users/HP/OneDrive/Desktop/ass2/src/com/portal/servlet/LoginServlet.java), [`LogoutServlet.java`](file:///c:/Users/HP/OneDrive/Desktop/ass2/src/com/portal/servlet/LogoutServlet.java), [`AuthFilter.java`](file:///c:/Users/HP/OneDrive/Desktop/ass2/src/com/portal/filter/AuthFilter.java), [`login.jsp`](file:///c:/Users/HP/OneDrive/Desktop/ass2/WebContent/login.jsp) | `HttpSession` creation, credential verification, 30-min timeout, automatic redirection to `login.jsp?error=unauthorized` on unauthenticated requests. |

---

## 📂 Project Architecture

```
ass2/
├── database.sql                             # MySQL Database creation and sample seed data
├── README.md                                # Full documentation and setup guide
├── WebContent/                              # Web application root
│   ├── index.jsp                            # Landing page (session-aware router)
│   ├── login.jsp                            # Login UI with alert feedback
│   ├── students.jsp                         # Student profile portal with dynamic looping
│   ├── error.jsp                            # Custom error handler page
│   ├── css/
│   │   └── style.css                        # Modern CSS styling for cards and layouts
│   ├── uploads/
│   │   ├── images/                          # Folder storing student photos (student1.jpg, etc.)
│   │   └── docs/                            # Folder storing student documents (marksheet1.pdf, etc.)
│   └── WEB-INF/
│       ├── web.xml                          # Deployment descriptor with servlet & filter mappings
│       ├── classes/                         # Compiled bytecode (.class files)
│       └── lib/
│           ├── jakarta.servlet-api-6.0.0.jar
│           └── mysql-connector-j-8.3.0.jar
└── src/
    └── com/
        └── portal/
            ├── model/
            │   ├── Student.java             # Student POJO / JavaBean
            │   └── User.java                # Admin / User POJO
            ├── util/
            │   └── DBConnection.java        # JDBC Connection provider
            ├── dao/
            │   ├── StudentDAO.java          # JDBC queries for students table
            │   └── UserDAO.java             # JDBC queries for users authentication
            ├── filter/
            │   └── AuthFilter.java          # Security filter intercepting unauthorized requests
            └── servlet/
                ├── LoginServlet.java        # Handles login and session creation
                ├── LogoutServlet.java       # Invalidates session and redirects
                ├── StudentListServlet.java  # Fetches students from DB and forwards to JSP
                ├── ImageStreamServlet.java  # Streams image bytes for <img> tags
                └── DocumentStreamServlet.java # Streams PDF/doc files for viewing/download
```

---

## 🛠️ Step-by-Step Setup Guide

### Step 1: MySQL Database Setup
1. Open MySQL Workbench, phpMyAdmin, or your MySQL Command Line Client.
2. Execute the provided script:
   ```bash
   mysql -u root -p < database.sql
   ```
3. Update database credentials in [`DBConnection.java`](file:///c:/Users/HP/OneDrive/Desktop/ass2/src/com/portal/util/DBConnection.java#L13-L15) if your MySQL user/password differs from `root` / `root`.

### Step 2: Running in Eclipse / NetBeans / IntelliJ
1. **Eclipse IDE for Enterprise Java and Web Developers**:
   - Go to `File` -> `Import` -> `General` -> `Existing Projects into Workspace` (or `File` -> `New` -> `Dynamic Web Project` and paste the `src` and `WebContent` folders).
   - Right-click the project -> `Properties` -> `Targeted Runtimes` -> Select **Apache Tomcat 10+** (or Tomcat 9).
   - Ensure the JARs in `WebContent/WEB-INF/lib` are added to the Build Path.
   - Right-click `index.jsp` or the project -> `Run As` -> `Run on Server`.

2. **Accessing the Portal in Browser**:
   - URL: `http://localhost:8080/ass2/` (or your configured context path)
   - **Default Login Credentials**:
     - Username: `admin` | Password: `admin123`
     - Username: `faculty` | Password: `faculty123`

---

## 🔄 Tomcat 10 (Jakarta) vs Tomcat 9 (javax) Compatibility

- The project is configured with **Jakarta EE** (`jakarta.servlet.*`), which is native to **Apache Tomcat 10 and 10.1+**.
- **If your college lab uses Apache Tomcat 9 or 8.5 (Java EE 8)**:
  Simply change the imports in the Servlet and Filter classes:
  - Replace: `import jakarta.servlet.*;`
  - With: `import javax.servlet.*;`
  - Replace `jakarta.servlet-api-6.0.0.jar` with `javax.servlet-api-4.0.1.jar` in `WebContent/WEB-INF/lib`.
