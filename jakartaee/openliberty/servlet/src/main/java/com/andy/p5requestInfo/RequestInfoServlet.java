package com.andy.p5requestInfo;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/request-info")
public class RequestInfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Retrieve query string parameters
        String name = request.getParameter("name");
        String age = request.getParameter("age");

        // Set an attribute in the request
        request.setAttribute("location", "Example City");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Generate response HTML using text blocks
        String htmlResponse = """
                <html>
                <head>
                    <title>Request Information Example</title>
                </head>
                <body>
                    <h1>Request Information</h1>
                    <p>Name: %s</p>
                    <p>Age: %s</p>
                    <p>Location: %s</p>
                    <p>HTTP Method: %s</p>
                    <p>Request URI: %s</p>
                    <p>Context Path: %s</p>
                    <p>Servlet Path: %s</p>
                    <p>Path Info: %s</p>
                </body>
                </html>
                """.formatted(
                name, age, request.getAttribute("location"),
                request.getMethod(), request.getRequestURI(),
                request.getContextPath(), request.getServletPath(),
                request.getPathInfo()
        );

        out.println(htmlResponse);
    }
}
