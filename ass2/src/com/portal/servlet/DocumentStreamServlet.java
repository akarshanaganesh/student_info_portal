package com.portal.servlet;

import com.portal.dao.StudentDAO;
import com.portal.model.Student;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;

/**
 * Servlet that retrieves and streams student documents (e.g. PDF marksheets, certificates).
 * Fulfills Task Requirement 2: Retrieve and stream images/documents from the folder where they are stored.
 */
@WebServlet(name = "DocumentStreamServlet", urlPatterns = {"/DocumentStreamServlet", "/documentStream"})
public class DocumentStreamServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StudentDAO studentDAO;

    @Override
    public void init() throws ServletException {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Student ID parameter is missing.");
            return;
        }

        try {
            int studentId = Integer.parseInt(idParam.trim());
            Student student = studentDAO.getStudentById(studentId);

            if (student == null || student.getDocumentPath() == null || student.getDocumentPath().trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No document associated with this student.");
                return;
            }

            String docFileName = student.getDocumentPath().trim();

            ServletContext context = getServletContext();
            String uploadsPath = context.getRealPath("/uploads/docs");

            File docFile = null;
            if (uploadsPath != null) {
                docFile = new File(uploadsPath, docFileName);
            }

            if (docFile == null || !docFile.exists()) {
                docFile = new File(docFileName);
            }

            if (!docFile.exists() || docFile.isDirectory()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Document file not found on server disk.");
                return;
            }

            String mimeType = context.getMimeType(docFile.getName());
            if (mimeType == null) {
                if (docFileName.toLowerCase().endsWith(".pdf")) {
                    mimeType = "application/pdf";
                } else if (docFileName.toLowerCase().endsWith(".doc")) {
                    mimeType = "application/msword";
                } else if (docFileName.toLowerCase().endsWith(".docx")) {
                    mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                } else {
                    mimeType = "application/octet-stream";
                }
            }

            String download = request.getParameter("download");
            boolean isDownload = "true".equalsIgnoreCase(download);
            String disposition = isDownload ? "attachment" : "inline";

            // Set Response Headers for browser PDF compatibility
            response.reset(); // clear any pre-set headers or buffers
            response.setContentType(mimeType);
            response.setContentLengthLong(docFile.length());
            response.setHeader("Content-Disposition", disposition + "; filename=\"" + docFile.getName() + "\"");
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Cache-Control", "private, max-age=3600");
            response.setHeader("Pragma", "private");

            try (InputStream in = new BufferedInputStream(new FileInputStream(docFile));
                 OutputStream out = new BufferedOutputStream(response.getOutputStream())) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid student ID.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error streaming document.");
        }
    }
}
