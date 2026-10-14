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
import java.nio.charset.StandardCharsets;

/**
 * Servlet that retrieves and streams student profile images.
 * Fulfills Task Requirement 2: Retrieve and stream images/documents from the folder where they are stored.
 * Fulfills Task Requirement 3: Serves image data for <img> tags pointing to this Servlet.
 */
@WebServlet(name = "ImageStreamServlet", urlPatterns = {"/ImageStreamServlet", "/imageStream"})
public class ImageStreamServlet extends HttpServlet {
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
            streamDefaultAvatar(response, "N/A");
            return;
        }

        try {
            int studentId = Integer.parseInt(idParam.trim());
            Student student = studentDAO.getStudentById(studentId);

            if (student == null || student.getPhotoPath() == null || student.getPhotoPath().trim().isEmpty()) {
                streamDefaultAvatar(response, student != null ? student.getName() : "Student");
                return;
            }

            String photoFileName = student.getPhotoPath().trim();

            ServletContext context = getServletContext();
            String uploadsPath = context.getRealPath("/uploads/images");
            
            File imageFile = null;
            if (uploadsPath != null) {
                imageFile = new File(uploadsPath, photoFileName);
            }

            if (imageFile == null || !imageFile.exists()) {
                imageFile = new File(photoFileName);
            }

            if (!imageFile.exists() || imageFile.isDirectory()) {
                streamDefaultAvatar(response, student.getName());
                return;
            }

            String mimeType = context.getMimeType(imageFile.getName());
            if (mimeType == null) {
                if (photoFileName.toLowerCase().endsWith(".png")) {
                    mimeType = "image/png";
                } else if (photoFileName.toLowerCase().endsWith(".gif")) {
                    mimeType = "image/gif";
                } else if (photoFileName.toLowerCase().endsWith(".svg")) {
                    mimeType = "image/svg+xml";
                } else {
                    mimeType = "image/jpeg";
                }
            }

            response.setContentType(mimeType);
            response.setContentLengthLong(imageFile.length());
            response.setHeader("Content-Disposition", "inline; filename=\"" + imageFile.getName() + "\"");
            response.setHeader("Cache-Control", "public, max-age=86400");

            try (InputStream in = new BufferedInputStream(new FileInputStream(imageFile));
                 OutputStream out = new BufferedOutputStream(response.getOutputStream())) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }

        } catch (NumberFormatException e) {
            streamDefaultAvatar(response, "Error");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error streaming image");
        }
    }

    private void streamDefaultAvatar(HttpServletResponse response, String name) throws IOException {
        String initial = (name != null && !name.isEmpty()) ? name.substring(0, 1).toUpperCase() : "?";
        
        String svg = "<svg xmlns='http://www.w3.org/2000/svg' width='120' height='120' viewBox='0 0 120 120'>" +
                     "<rect width='120' height='120' rx='60' fill='#2563EB'/>" +
                     "<text x='50%' y='55%' dominant-baseline='middle' text-anchor='middle' " +
                     "fill='#FFFFFF' font-family='Arial, sans-serif' font-weight='bold' font-size='48'>" +
                     initial + "</text></svg>";

        byte[] svgBytes = svg.getBytes(StandardCharsets.UTF_8);
        response.setContentType("image/svg+xml");
        response.setContentLength(svgBytes.length);
        response.setHeader("Content-Disposition", "inline; filename=\"avatar.svg\"");
        
        try (OutputStream out = response.getOutputStream()) {
            out.write(svgBytes);
            out.flush();
        }
    }
}
