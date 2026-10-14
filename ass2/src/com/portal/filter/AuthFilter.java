package com.portal.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter to enforce authentication and session management.
 * Fulfills Task Requirement 5: "Ensure session handling so that only logged-in users can access the portal securely."
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        HttpSession session = httpRequest.getSession(false);

        // Prevent browser caching of sensitive HTML/JSP pages (allow document/image streams to be rendered by PDF viewer)
        if (!requestURI.contains("StreamServlet") && !requestURI.contains("Stream")) {
            httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
            httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0
            httpResponse.setDateHeader("Expires", 0); // Proxies
        }

        boolean isLoggedIn = (session != null && session.getAttribute("currentUser") != null);

        // Public URLs that do not require login
        boolean isLoginJsp = requestURI.endsWith("login.jsp") || requestURI.endsWith("/");
        boolean isLoginServlet = requestURI.endsWith("LoginServlet") || requestURI.endsWith("/login");
        boolean isStaticAsset = requestURI.contains("/css/") || requestURI.endsWith(".css") ||
                                requestURI.contains("/js/") || requestURI.endsWith(".js") ||
                                requestURI.endsWith(".ico") || requestURI.endsWith(".png") ||
                                requestURI.endsWith(".jpg");

        if (isLoggedIn || isLoginJsp || isLoginServlet || isStaticAsset) {
            // User is authorized or accessing public resource
            chain.doFilter(request, response);
        } else {
            // Unauthorized access: redirect to login page with alert parameter
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp?error=unauthorized");
        }
    }

    @Override
    public void destroy() {
        // Cleanup logic
    }
}
