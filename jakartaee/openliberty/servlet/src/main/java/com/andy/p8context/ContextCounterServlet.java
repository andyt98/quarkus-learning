package com.andy.p8context;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/context-counter")
public class ContextCounterServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        ServletContext context = getServletContext();

        // Retrieve or create the counter attribute
        Integer counter = (Integer) context.getAttribute("requestCounter");
        if (counter == null) {
            counter = 0;
        }

        // Increment the counter
        counter++;

        // Store the updated counter back in the context
        context.setAttribute("requestCounter", counter);

        // Log the counter value
        Logger.getLogger(ContextCounterServlet.class.getName()).info("Request Counter: " + counter);

        response.getWriter().write("Request Counter: " + counter);
    }
}
