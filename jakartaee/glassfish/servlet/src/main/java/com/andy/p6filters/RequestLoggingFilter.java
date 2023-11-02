package com.andy.p6filters;

import com.andy.p3listener.MyContextListener;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter("/hello")
public class RequestLoggingFilter implements Filter {

    private static final Logger logger = Logger.getLogger(MyContextListener.class.getName());

    @Override
    public void init(FilterConfig filterConfig) {
        // Initialization code, if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Log incoming request information
        String clientIP = request.getRemoteAddr();
        String contextPath = request.getServletContext().getContextPath();
        String servletPath = ((HttpServletRequest) request).getServletPath();
        String pathInfo = ((HttpServletRequest) request).getPathInfo();

        String requestURL = contextPath + servletPath + (pathInfo != null ? pathInfo : "");

        logger.log(Level.INFO, "Incoming request from " + clientIP + " for: " + requestURL);

        // Continue the filter chain
        chain.doFilter(request, response);
    }


    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
