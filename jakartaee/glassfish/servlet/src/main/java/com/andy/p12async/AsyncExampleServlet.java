package com.andy.p12async;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/async"}, asyncSupported = true)
public class AsyncExampleServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AsyncExampleServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        LOGGER.info("Servlet processing started.");

        // Start asynchronous processing
        final AsyncContext asyncContext = request.startAsync();

        // Simulate a long-running operation
        asyncContext.start(() -> {
            try {
                LOGGER.info("Async operation started.");
                Thread.sleep(5000); // Simulate a 5-second delay (long-running operation)
                String result = "Long-running operation completed";

                // Write response to the client
                HttpServletResponse asyncResponse = (HttpServletResponse) asyncContext.getResponse();
                asyncResponse.getWriter().write(result);

                LOGGER.info("Async operation completed.");

                // Complete the asynchronous processing
                asyncContext.complete();
            } catch (InterruptedException | IOException e) {
                LOGGER.log(Level.SEVERE, "Error during async processing.", e);
            }
        });

        LOGGER.info("Processing completed.");
    }
}
