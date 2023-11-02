package com.andy.p2lifecycle;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet("/lifecycle")
public class LifecycleExampleServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LifecycleExampleServlet.class.getName());

    public LifecycleExampleServlet() {
        logger.log(Level.INFO, "Servlet instance created (constructor)");
    }

    @Override
    public void init() {
        logger.log(Level.INFO, "Servlet initialization (init() method)");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.log(Level.INFO, "Service method called");

        // Set content type and write a response
        response.setContentType("text/html");
        response.getWriter().println("""
            <html>
                <body>
                    <h2>Servlet Lifecycle Example</h2>
                </body>
            </html>
        """);
    }

    @Override
    public void destroy() {
        logger.log(Level.INFO, "Servlet destroyed (destroy() method)");
    }
}
