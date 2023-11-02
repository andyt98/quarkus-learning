package com.andy.p10finalizing;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/finalizing")
public class FinalizingServlet extends HttpServlet {

    private int serviceCounter = 0;
    private boolean shuttingDown = false;
    private static final int interval = 1000; // Interval for waiting in milliseconds
    private static final Logger logger = Logger.getLogger(FinalizingServlet.class.getName());

    protected synchronized void enteringServiceMethod() {
        serviceCounter++;
        logger.log(Level.INFO, "Entering service method. Active services: " + numServices());
    }

    protected synchronized void leavingServiceMethod() {
        serviceCounter--;
        logger.log(Level.INFO, "Leaving service method. Active services: " + numServices());
    }

    protected synchronized int numServices() {
        return serviceCounter;
    }

    protected synchronized void setShuttingDown() {
        shuttingDown = true;
        logger.log(Level.INFO, "Shutting down. Active services: " + numServices());
    }

    protected synchronized boolean isShuttingDown() {
        return shuttingDown;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        enteringServiceMethod();
        try {
            super.service(req, resp);
        } finally {
            leavingServiceMethod();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        logger.log(Level.INFO, "Long-running operation started.");
        // Simulate a long-running operation
        for (int i = 0; (i < 10) && !isShuttingDown(); i++) {
            try {
                Thread.sleep(500); // Sleep for 500 milliseconds
                logger.log(Level.INFO, "Processing step: " + i);
            } catch (InterruptedException e) {
                // Handle interruption and cleanup
                logger.log(Level.SEVERE, "Interrupted during processing", e);
                break;
            }
        }

        response.getWriter().write("Long-running operation completed");
        logger.log(Level.INFO, "Long-running operation completed.");
    }

    @Override
    public void destroy() {
        logger.log(Level.INFO, "Destroying servlet.");
        if (numServices() > 0) {
            setShuttingDown();
        }

        while (numServices() > 0) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Error during shutdown", e);
            }
        }
        logger.log(Level.INFO, "Servlet destroyed.");
    }
}
